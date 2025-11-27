package com.example.PasteleriaBackend.service;

import com.example.PasteleriaBackend.web.dto.LoginRequest;
import com.example.PasteleriaBackend.web.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
