package com.astracommerce.inventoryservice.service;

import com.astracommerce.inventoryservice.dto.InventoryResponse;

public interface InventoryService {
    InventoryResponse getByProductId(Long productId);
    InventoryResponse initializeInventory(Long productId, int quantity);
    InventoryResponse decrementStock(Long productId, int quantity);
    InventoryResponse incrementStock(Long productId, int quantity);
    boolean isInStock(Long productId, int quantity);
}
