package com.example.PasteleriaBackend.web.dto;

import java.util.List;

public class BlogContentBlockResponse {

    private final String type;
    private final String content;
    private final List<String> items;

    public BlogContentBlockResponse(String type, String content, List<String> items) {
        this.type = type;
        this.content = content;
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public List<String> getItems() {
        return items;
    }
}
