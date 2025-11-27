package com.example.PasteleriaBackend.web.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerAddressRequest(
    UUID id,
    @Size(max = 80) String alias,
    @NotBlank @Size(max = 255) String direccion,
    @NotBlank @Size(max = 100) String region,
    @NotBlank @Size(max = 100) String comuna,
    @Size(max = 255) String referencia,
    boolean primary
) {
}
