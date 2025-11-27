package com.example.PasteleriaBackend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.Product;
import com.example.PasteleriaBackend.domain.repository.ProductRepository;
import com.example.PasteleriaBackend.service.ProductService;
import com.example.PasteleriaBackend.web.dto.ProductRequest;
import com.example.PasteleriaBackend.web.dto.ProductResponse;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAllActive() {
        return productRepository.findByActiveTrue().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(String id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return toResponse(product);
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsById(request.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con ese ID");
        }
        Product product = new Product();
        applyRequest(product, request);
        product.setId(request.getId());
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse update(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        applyRequest(product, request);
        return toResponse(productRepository.save(product));
    }

    @Override
    public void delete(String id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        product.setActive(false);
        productRepository.save(product);
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setAttributes(request.getAttributes());
        product.setImageUrl(request.getImageUrl());
        product.setStock(request.getStock());
        product.setCriticalStock(request.getCriticalStock());
        product.setDescription(request.getDescription());
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getCategory(),
            product.getAttributes(),
            product.getImageUrl(),
            product.getStock(),
            product.getCriticalStock(),
            product.getDescription(),
            product.isActive()
        );
    }
}
