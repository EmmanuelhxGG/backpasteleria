package com.example.PasteleriaBackend.web.dto;

public class LoginResponse {

    private String token;
    private long expiresAt;
    private String role;
    private String userId;

    public LoginResponse(String token, long expiresAt, String role, String userId) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public String getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }
}
