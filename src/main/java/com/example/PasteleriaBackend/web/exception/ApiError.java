package com.example.PasteleriaBackend.web.exception;

import java.time.OffsetDateTime;
import java.util.List;

public class ApiError {

    private final OffsetDateTime timestamp;
    private final int status;
    private final String message;
    private final List<String> details;

    public ApiError(int status, String message, List<String> details) {
        this.timestamp = OffsetDateTime.now();
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
