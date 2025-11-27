package com.example.PasteleriaBackend.config.seed;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.Product;
import com.example.PasteleriaBackend.domain.repository.ProductRepository;

@Component
@Order(40)
@Transactional
public class ProductSeeder implements ApplicationRunner {

    private final ProductRepository productRepository;

    public ProductSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (productRepository.count() > 0) {
            return;
        }

        Product tortaChocolate = createProduct(
            "TC001",
            "Torta Cuadrada de Chocolate",
            45000d,
            "Tortas Cuadradas",
            "20 porciones",
            "/img/Torta Cuadrada de Chocolate.png",
            10,
            2,
            "Bizcocho húmedo de chocolate con relleno de ganache y avellanas tostadas."
        );

        Product tortaFrutas = createProduct(
            "TC002",
            "Torta Cuadrada de Frutas",
            50000d,
            "Tortas Cuadradas",
            "Frutas frescas",
            "/img/Torta Cuadrada de Frutas.png",
            8,
            2,
            "Base de vainilla con crema diplomática y selección de frutas de temporada."
        );

        Product mousseChocolate = createProduct(
            "PI001",
            "Mousse de Chocolate",
            5000d,
            "Postres Individuales",
            "Individual",
            "/img/Mousse de Chocolate.png",
            30,
            5,
            "Mousse aireada de cacao 60% con chips crujientes de chocolate."
        );

        productRepository.saveAll(List.of(tortaChocolate, tortaFrutas, mousseChocolate));
    }

    private Product createProduct(
        String id,
        String name,
        Double price,
        String category,
        String attributes,
        String imageUrl,
        Integer stock,
        Integer criticalStock,
        String description
    ) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setAttributes(attributes);
        product.setImageUrl(imageUrl);
        product.setStock(stock);
        product.setCriticalStock(criticalStock);
        product.setDescription(description);
        product.setActive(true);
        return product;
    }
}
