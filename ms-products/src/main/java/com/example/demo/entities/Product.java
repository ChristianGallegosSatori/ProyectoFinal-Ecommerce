package com.example.demo.entities;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Asegura que no se repita el código de barras/SKU
    private String sku;

    private String name;

    @Column(length = 1000)
    private String description;

    private Double price;
    private Integer stock;
    private String category;
}
