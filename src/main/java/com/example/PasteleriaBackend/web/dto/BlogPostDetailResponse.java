package com.example.PasteleriaBackend.web.dto;

import java.util.List;

public class BlogPostDetailResponse {

    private final String id;
    private final String slug;
    private final String title;
    private final BlogHeroResponse hero;
    private final String excerpt;
    private final List<BlogContentBlockResponse> body;
    private final List<BlogCommentResponse> comments;

    public BlogPostDetailResponse(String id, String slug, String title, BlogHeroResponse hero, String excerpt,
            List<BlogContentBlockResponse> body, List<BlogCommentResponse> comments) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.hero = hero;
        this.excerpt = excerpt;
        this.body = body;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public BlogHeroResponse getHero() {
        return hero;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public List<BlogContentBlockResponse> getBody() {
        return body;
    }

    public List<BlogCommentResponse> getComments() {
        return comments;
    }
}
