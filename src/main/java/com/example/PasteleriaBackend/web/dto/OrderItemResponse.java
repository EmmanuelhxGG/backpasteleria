package com.example.PasteleriaBackend.web.dto;

import java.util.List;

public record OrderItemResponse(
    String codigo,
    String nombre,
    int quantity,
    double unitPrice,
    double originalUnitPrice,
    double discountPerUnit,
    double subtotal,
    double originalSubtotal,
    List<String> benefitLabels
) {
}
