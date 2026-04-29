package com.astracommerce.inventoryservice.controller;

import com.astracommerce.inventoryservice.dto.InventoryResponse;
import com.astracommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getByProductId(productId));
    }

    @PostMapping("/{productId}/initialize")
    public ResponseEntity<InventoryResponse> initialize(
            @PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.initializeInventory(productId, quantity));
    }

    @PatchMapping("/{productId}/increment")
    public ResponseEntity<InventoryResponse> increment(
            @PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.incrementStock(productId, quantity));
    }

    @GetMapping("/{productId}/check")
    public ResponseEntity<Boolean> checkStock(
            @PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.isInStock(productId, quantity));
    }
}
