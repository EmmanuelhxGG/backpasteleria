package com.example.PasteleriaBackend.web.dto;

public class BlogPostSummaryResponse {

    private final String id;
    private final String slug;
    private final String title;
    private final BlogHeroResponse hero;
    private final String excerpt;

    public BlogPostSummaryResponse(String id, String slug, String title, BlogHeroResponse hero, String excerpt) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.hero = hero;
        this.excerpt = excerpt;
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
}
