package com.example.demo.clients;

import com.example.demo.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "ms-products",
        url = "${clients.products.url}",
        fallback = ProductClientFallback.class
)
public interface ProductClient {

    @GetMapping("/api/v1/products")
    List<ProductDto> getAllProducts();

    @GetMapping("/api/v1/products/sku/{sku}")
    ProductDto getProductBySku(@PathVariable("sku") String sku);
}
