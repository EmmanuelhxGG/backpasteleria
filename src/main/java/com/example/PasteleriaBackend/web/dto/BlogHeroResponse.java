package com.example.PasteleriaBackend.web.dto;

public class BlogHeroResponse {

    private final String image;
    private final String caption;

    public BlogHeroResponse(String image, String caption) {
        this.image = image;
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public String getCaption() {
        return caption;
    }
}
