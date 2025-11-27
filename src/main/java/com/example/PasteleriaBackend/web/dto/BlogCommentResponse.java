package com.example.PasteleriaBackend.web.dto;

import java.time.OffsetDateTime;

public class BlogCommentResponse {

    private final String id;
    private final String postId;
    private final String authorName;
    private final String authorEmail;
    private final String content;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime editedAt;

    public BlogCommentResponse(String id, String postId, String authorName, String authorEmail, String content,
            OffsetDateTime createdAt, OffsetDateTime editedAt) {
        this.id = id;
        this.postId = postId;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.content = content;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }

    public String getId() {
        return id;
    }

    public String getPostId() {
        return postId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getEditedAt() {
        return editedAt;
    }
}
