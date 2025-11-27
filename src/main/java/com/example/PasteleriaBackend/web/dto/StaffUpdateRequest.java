package com.example.PasteleriaBackend.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record StaffUpdateRequest(
    @Size(max = 100) String firstName,
    @Size(max = 120) String lastName,
    @Email @Size(max = 150) String email,
    @Size(min = 6, max = 60) String password,
    @Size(max = 60) String staffRole,
    @Size(max = 100) String region,
    @Size(max = 100) String commune,
    @Size(max = 255) String address,
    @Size(max = 30) String phone,
    Boolean active
) {
}
