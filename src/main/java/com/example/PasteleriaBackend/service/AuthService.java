package com.example.PasteleriaBackend.service;

import com.example.PasteleriaBackend.web.dto.LoginRequest;
import com.example.PasteleriaBackend.web.dto.LoginResponse;
import com.example.PasteleriaBackend.web.dto.TokenRefreshRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse refresh(TokenRefreshRequest request);
}
