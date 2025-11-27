package com.example.PasteleriaBackend.web.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
    @NotBlank String productId,
    @Min(1) int quantity,
    @NotNull Double unitPrice,
    @NotNull Double originalUnitPrice,
    @NotNull Double discountPerUnit,
    List<String> benefitLabels,
    String note
) {
}
