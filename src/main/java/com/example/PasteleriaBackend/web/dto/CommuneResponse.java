package com.example.PasteleriaBackend.web.dto;

public class CommuneResponse {

    private final Long id;
    private final String name;

    public CommuneResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
