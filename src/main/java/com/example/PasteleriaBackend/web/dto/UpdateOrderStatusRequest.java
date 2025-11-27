package com.example.PasteleriaBackend.web.dto;

import com.example.PasteleriaBackend.domain.model.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
    @NotNull OrderStatus status,
    String notes
) {
}
