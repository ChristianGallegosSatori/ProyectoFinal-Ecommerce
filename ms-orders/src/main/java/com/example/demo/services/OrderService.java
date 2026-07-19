package com.example.demo.services;

import com.example.demo.clients.ProductClient;
import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.entities.Order;
import com.example.demo.repositories.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderService(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    public Order createOrder(OrderRequest request) {
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        }

        ProductDto product = fetchProduct(request.productSku());
        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo obtener información del producto '" + request.productSku() + "'. Intente más tarde."
            );
        }

        Order order = new Order();
        order.setProductSku(product.getSku());
        order.setProductName(product.getName());
        order.setUnitPrice(product.getPrice());
        order.setQuantity(request.quantity());
        order.setTotalPrice(product.getPrice() * request.quantity());

        return orderRepository.save(order);
    }

    // Punto de integración entre ms-orders y ms-products, protegido con Circuit Breaker + Retry.
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackFetchProduct")
    @Retry(name = "productsRetry")
    public ProductDto fetchProduct(String sku) {
        return productClient.getProductBySku(sku);
    }

    private ProductDto fallbackFetchProduct(String sku, Throwable t) {
        log.warn("Fallback de ms-products activado para sku='{}'. Causa: {}", sku, t.toString());
        return null;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
