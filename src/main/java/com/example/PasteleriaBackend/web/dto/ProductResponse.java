package com.example.PasteleriaBackend.web.dto;

public class ProductResponse {

    private final String id;
    private final String name;
    private final double price;
    private final String category;
    private final String attributes;
    private final String imageUrl;
    private final int stock;
    private final int criticalStock;
    private final String description;
    private final boolean active;

    public ProductResponse(String id, String name, double price, String category, String attributes,
            String imageUrl, int stock, int criticalStock, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.attributes = attributes;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.criticalStock = criticalStock;
        this.description = description;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getAttributes() {
        return attributes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }

    public int getCriticalStock() {
        return criticalStock;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }
}
