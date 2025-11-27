package com.example.PasteleriaBackend.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StaffRequest(
    @NotBlank @Size(max = 20) String run,
    @NotBlank @Size(max = 100) String firstName,
    @NotBlank @Size(max = 120) String lastName,
    @NotBlank @Email @Size(max = 150) String email,
    @NotBlank @Size(min = 6, max = 60) String password,
    @Size(max = 60) String staffRole,
    @Size(max = 100) String region,
    @Size(max = 100) String commune,
    @Size(max = 255) String address,
    @Size(max = 30) String phone
) {
}
