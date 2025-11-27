package com.example.PasteleriaBackend.service;

import java.util.List;

import com.example.PasteleriaBackend.web.dto.BlogPostDetailResponse;
import com.example.PasteleriaBackend.web.dto.BlogPostSummaryResponse;

public interface BlogService {

    List<BlogPostSummaryResponse> findAll();

    BlogPostDetailResponse findBySlug(String slug);
}
