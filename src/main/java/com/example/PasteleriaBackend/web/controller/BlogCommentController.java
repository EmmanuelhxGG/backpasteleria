package com.example.PasteleriaBackend.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PasteleriaBackend.domain.model.RoleName;
import com.example.PasteleriaBackend.security.UserPrincipal;
import com.example.PasteleriaBackend.service.BlogCommentService;
import com.example.PasteleriaBackend.web.dto.BlogCommentCreateRequest;
import com.example.PasteleriaBackend.web.dto.BlogCommentResponse;
import com.example.PasteleriaBackend.web.dto.BlogCommentUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/blog")
@Validated
@Tag(name = "Comentarios del blog")
public class BlogCommentController {

    private final BlogCommentService blogCommentService;

    
    public BlogCommentController(BlogCommentService blogCommentService) {
        this.blogCommentService = blogCommentService;
    }

    @PostMapping("/posts/{slug}/comments")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @Operation(summary = "Agregar un comentario a una entrada del blog")
    public ResponseEntity<BlogCommentResponse> create(
        @PathVariable String slug,
        @AuthenticationPrincipal UserPrincipal principal,
        @Valid @RequestBody BlogCommentCreateRequest request
    ) {
        return new ResponseEntity<>(blogCommentService.addComment(principal.getId(), slug, request), HttpStatus.CREATED);
    }

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @Operation(summary = "Actualizar un comentario existente")
    public ResponseEntity<BlogCommentResponse> update(
        @PathVariable String commentId,
        @AuthenticationPrincipal UserPrincipal principal,
        @Valid @RequestBody BlogCommentUpdateRequest request
    ) {
        boolean canModerate = principal.getRole() == RoleName.ADMIN;
        return ResponseEntity.ok(blogCommentService.updateComment(commentId, principal.getId(), canModerate, request));
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @Operation(summary = "Eliminar un comentario del blog")
    public ResponseEntity<Void> delete(
        @PathVariable String commentId,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        boolean canModerate = principal.getRole() == RoleName.ADMIN;
        blogCommentService.deleteComment(commentId, principal.getId(), canModerate);
        return ResponseEntity.noContent().build();
    }
}
