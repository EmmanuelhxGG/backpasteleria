package com.example.PasteleriaBackend.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PasteleriaBackend.service.BlogService;
import com.example.PasteleriaBackend.web.dto.BlogPostDetailResponse;
import com.example.PasteleriaBackend.web.dto.BlogPostSummaryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/blog")
@Tag(name = "Blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/posts")
    @Operation(summary = "Listar entradas del blog", description = "Retorna las entradas del blog ordenadas por fecha")
    public ResponseEntity<List<BlogPostSummaryResponse>> listPosts() {
        return ResponseEntity.ok(blogService.findAll());
    }

    @GetMapping("/posts/{slug}")
    @Operation(summary = "Detalle de entrada del blog", description = "Obtiene el detalle y comentarios de una entrada por su slug")
    public ResponseEntity<BlogPostDetailResponse> getPost(@PathVariable String slug) {
        return ResponseEntity.ok(blogService.findBySlug(slug));
    }
}
