package com.example.PasteleriaBackend.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BlogCommentUpdateRequest(
    @NotBlank @Size(max = 500) String content
) {
}
