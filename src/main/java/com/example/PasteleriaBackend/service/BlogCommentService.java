package com.example.PasteleriaBackend.service;

import com.example.PasteleriaBackend.web.dto.BlogCommentCreateRequest;
import com.example.PasteleriaBackend.web.dto.BlogCommentResponse;
import com.example.PasteleriaBackend.web.dto.BlogCommentUpdateRequest;

public interface BlogCommentService {

    BlogCommentResponse addComment(String userId, String postSlug, BlogCommentCreateRequest request);

    BlogCommentResponse updateComment(String commentId, String userId, boolean canModerate, BlogCommentUpdateRequest request);

    void deleteComment(String commentId, String userId, boolean canModerate);
}
