package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String sku;

    private String name;
    private String description;

    private Double price;
    private Integer stock;
    private String category;
}
