package com.astracommerce.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Fallback controller for Circuit Breaker responses.
 * Returns a graceful degraded response when a downstream service is unavailable.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user-service")
    public ResponseEntity<Map<String, String>> userServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "User Service is temporarily unavailable. Please try again later."));
    }

    @GetMapping("/product-service")
    public ResponseEntity<Map<String, String>> productServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Product Service is temporarily unavailable. Please try again later."));
    }

    @GetMapping("/order-service")
    public ResponseEntity<Map<String, String>> orderServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Order Service is temporarily unavailable. Please try again later."));
    }

    @GetMapping("/inventory-service")
    public ResponseEntity<Map<String, String>> inventoryServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Inventory Service is temporarily unavailable. Please try again later."));
    }
}
