package com.example.PasteleriaBackend.web.dto;

public class LoginResponse {

    private String token;
    private long expiresAt;
    private String refreshToken;
    private long refreshExpiresAt;
    private String role;
    private String userId;

    public LoginResponse(String token, long expiresAt, String refreshToken, long refreshExpiresAt, String role, String userId) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.refreshToken = refreshToken;
        this.refreshExpiresAt = refreshExpiresAt;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getRefreshExpiresAt() {
        return refreshExpiresAt;
    }

    public String getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }
}
