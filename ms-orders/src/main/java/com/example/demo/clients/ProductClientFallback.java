package com.example.demo.clients;

import com.example.demo.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public List<ProductDto> getAllProducts() {
        return Collections.emptyList();
    }

    @Override
    public ProductDto getProductBySku(String sku) {
        return null;
    }
}
