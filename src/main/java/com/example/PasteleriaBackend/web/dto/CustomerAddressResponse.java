package com.example.PasteleriaBackend.web.dto;

public record CustomerAddressResponse(
    String id,
    String alias,
    String direccion,
    String region,
    String comuna,
    String referencia,
    boolean primary,
    long createdAt,
    long updatedAt
) {
}
