package com.example.demo.services;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AiService aiService;

    public ProductService(ProductRepository productRepository, AiService aiService) {
        this.productRepository = productRepository;
        this.aiService = aiService;
    }


    public Product createProduct(Product product) {
        if(product.getDescription() == null || product.getDescription().isBlank()) {
            String generateDescription = aiService.generateProductDescription(product.getName(), product.getCategory());
            product.setDescription(generateDescription);
        }

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }
}
