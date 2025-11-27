package com.example.PasteleriaBackend.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.PasteleriaBackend.domain.model.BlogComment;
import com.example.PasteleriaBackend.domain.model.BlogPost;
import com.example.PasteleriaBackend.domain.model.Product;
import com.example.PasteleriaBackend.domain.model.User;
import com.example.PasteleriaBackend.domain.model.UserStatus;
import com.example.PasteleriaBackend.domain.repository.BlogCommentRepository;
import com.example.PasteleriaBackend.domain.repository.BlogPostRepository;
import com.example.PasteleriaBackend.domain.repository.ProductRepository;
import com.example.PasteleriaBackend.domain.repository.UserRepository;
import com.example.PasteleriaBackend.service.BlogCommentService;
import com.example.PasteleriaBackend.web.dto.BlogCommentCreateRequest;
import com.example.PasteleriaBackend.web.dto.BlogCommentResponse;
import com.example.PasteleriaBackend.web.dto.BlogCommentUpdateRequest;

@Service
@Transactional
public class BlogCommentServiceImpl implements BlogCommentService {

    private static final String PRODUCT_THREAD_PREFIX = "product:";

    private final BlogPostRepository blogPostRepository;
    private final BlogCommentRepository blogCommentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public BlogCommentServiceImpl(
        BlogPostRepository blogPostRepository,
        BlogCommentRepository blogCommentRepository,
        UserRepository userRepository,
        ProductRepository productRepository
    ) {
        this.blogPostRepository = blogPostRepository;
        this.blogCommentRepository = blogCommentRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public BlogCommentResponse addComment(String userId, String postSlug, BlogCommentCreateRequest request) {
        User author = findActiveUser(userId);
        BlogPost post = resolveTargetPost(postSlug);
        if (post == null) {
            throw new IllegalArgumentException("Entrada de blog no encontrada");
        }

        BlogComment comment = new BlogComment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setAuthorName(buildAuthorName(author));
        comment.setAuthorEmail(author.getEmail());
        comment.setContent(normalizeContent(request.content()));

        blogCommentRepository.save(comment);
        return toResponse(comment);
    }

    @Override
    public BlogCommentResponse updateComment(String commentId, String userId, boolean canModerate, BlogCommentUpdateRequest request) {
        BlogComment comment = findComment(commentId);
        User user = findActiveUser(userId);
        if (!canModerate && !comment.getAuthor().getId().equals(user.getId())) {
            throw new IllegalStateException("Solo el autor o un administrador puede editar el comentario");
        }
        comment.setContent(normalizeContent(request.content()));
        return toResponse(comment);
    }

    @Override
    public void deleteComment(String commentId, String userId, boolean canModerate) {
        BlogComment comment = findComment(commentId);
        User user = findActiveUser(userId);
        if (!canModerate && !comment.getAuthor().getId().equals(user.getId())) {
            throw new IllegalStateException("Solo el autor o un administrador puede eliminar el comentario");
        }
        blogCommentRepository.delete(comment);
    }

    private BlogComment findComment(String commentId) {
        UUID id = parseUuid(commentId, "Identificador de comentario inválido");
        return blogCommentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));
    }

    private BlogPost resolveTargetPost(String slug) {
        return blogPostRepository.findBySlug(slug)
            .orElseGet(() -> maybeCreateProductThread(slug));
    }

    private BlogPost maybeCreateProductThread(String slug) {
        if (!StringUtils.hasText(slug) || !slug.startsWith(PRODUCT_THREAD_PREFIX)) {
            return null;
        }
        String productId = slug.substring(PRODUCT_THREAD_PREFIX.length());
        if (!StringUtils.hasText(productId)) {
            return null;
        }
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        BlogPost placeholder = new BlogPost();
        placeholder.setSlug(slug);
        placeholder.setTitle("Opiniones sobre " + product.getName());
        String hero = StringUtils.hasText(product.getImageUrl()) ? product.getImageUrl() : "/img/placeholder.png";
        placeholder.setHeroImage(hero);
        placeholder.setHeroCaption(StringUtils.hasText(product.getCategory()) ? product.getCategory() : null);
        String excerpt = StringUtils.hasText(product.getDescription())
            ? product.getDescription()
            : "Comparte tu experiencia con " + product.getName();
        placeholder.setExcerpt(excerpt);
        placeholder.setBody("[]");
        return blogPostRepository.save(placeholder);
    }

    private User findActiveUser(String userId) {
        UUID id = parseUuid(userId, "Identificador de usuario inválido");
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("El usuario no está habilitado para comentar");
        }
        return user;
    }

    private UUID parseUuid(String value, String message) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(message, ex);
        }
    }

    private String buildAuthorName(User user) {
        String firstName = StringUtils.hasText(user.getFirstName()) ? user.getFirstName().trim() : "";
        String lastName = StringUtils.hasText(user.getLastName()) ? user.getLastName().trim() : "";
        String name = (firstName + " " + lastName).trim();
        return name.isEmpty() ? user.getEmail() : name;
    }

    private String normalizeContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("El contenido del comentario es obligatorio");
        }
        return content.trim();
    }

    
    private BlogCommentResponse toResponse(BlogComment comment) {
        return new BlogCommentResponse(
            comment.getId().toString(),
            comment.getPost().getId().toString(),
            comment.getAuthorName(),
            comment.getAuthorEmail(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getEditedAt()
        );
    }
}
