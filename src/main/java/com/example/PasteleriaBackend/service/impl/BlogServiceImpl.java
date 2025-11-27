package com.example.PasteleriaBackend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.BlogPost;
import com.example.PasteleriaBackend.domain.repository.BlogCommentRepository;
import com.example.PasteleriaBackend.domain.repository.BlogPostRepository;
import com.example.PasteleriaBackend.service.BlogService;
import com.example.PasteleriaBackend.web.dto.BlogCommentResponse;
import com.example.PasteleriaBackend.web.dto.BlogContentBlockResponse;
import com.example.PasteleriaBackend.web.dto.BlogHeroResponse;
import com.example.PasteleriaBackend.web.dto.BlogPostDetailResponse;
import com.example.PasteleriaBackend.web.dto.BlogPostSummaryResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(readOnly = true)
public class BlogServiceImpl implements BlogService {

    private final BlogPostRepository blogPostRepository;
    private final BlogCommentRepository blogCommentRepository;
    private final ObjectMapper objectMapper;

    public BlogServiceImpl(
            BlogPostRepository blogPostRepository,
            BlogCommentRepository blogCommentRepository,
            ObjectMapper objectMapper) {
        this.blogPostRepository = blogPostRepository;
        this.blogCommentRepository = blogCommentRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<BlogPostSummaryResponse> findAll() {
        return blogPostRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
            .map(this::toSummary)
            .collect(Collectors.toList());
    }

    @Override
    public BlogPostDetailResponse findBySlug(String slug) {
        BlogPost post = blogPostRepository.findBySlug(slug)
            .orElseThrow(() -> new IllegalArgumentException("Entrada de blog no encontrada"));

        BlogPostSummaryResponse summary = toSummary(post);
        List<BlogContentBlockResponse> body = parseBody(post.getBody());
        String postId = post.getId().toString();

        List<BlogCommentResponse> comments = blogCommentRepository.findByPostIdOrderByCreatedAtAsc(post.getId()).stream()
            .map(comment -> new BlogCommentResponse(
                comment.getId().toString(),
                postId,
                comment.getAuthorName(),
                comment.getAuthorEmail(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getEditedAt()))
            .collect(Collectors.toList());

        return new BlogPostDetailResponse(
            summary.getId(),
            summary.getSlug(),
            summary.getTitle(),
            summary.getHero(),
            summary.getExcerpt(),
            body,
            comments
        );
    }

    private BlogPostSummaryResponse toSummary(BlogPost post) {
        BlogHeroResponse hero = new BlogHeroResponse(post.getHeroImage(), post.getHeroCaption());
        return new BlogPostSummaryResponse(
            post.getId().toString(),
            post.getSlug(),
            post.getTitle(),
            hero,
            post.getExcerpt()
        );
    }

    private List<BlogContentBlockResponse> parseBody(String bodyJson) {
        if (bodyJson == null || bodyJson.isBlank()) {
            return List.of();
        }
        try {
            JsonNode root = objectMapper.readTree(bodyJson);
            if (!root.isArray()) {
                return List.of();
            }
            List<BlogContentBlockResponse> blocks = new ArrayList<>();
            for (JsonNode element : root) {
                String type = element.path("type").asText();
                JsonNode contentNode = element.get("content");
                String content = contentNode != null && contentNode.isTextual() ? contentNode.asText() : null;
                List<String> items = null;
                if (contentNode != null && contentNode.isArray()) {
                    items = new ArrayList<>();
                    for (JsonNode itemNode : contentNode) {
                        items.add(itemNode.asText());
                    }
                }
                blocks.add(new BlogContentBlockResponse(type, content, items));
            }
            return blocks;
        } catch (JsonProcessingException ex) {
            // Propagamos un error controlado cuando el JSON almacenado es inválido.
            throw new IllegalStateException("No se pudo procesar el contenido del blog", ex);
        }
    }
}
