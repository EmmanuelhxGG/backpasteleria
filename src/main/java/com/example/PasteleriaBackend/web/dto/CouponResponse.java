package com.example.PasteleriaBackend.web.dto;

public class CouponResponse {

    private final String code;
    private final String type;
    private final double value;
    private final String label;

    public CouponResponse(String code, String type, double value, String label) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
