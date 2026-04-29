package com.astracommerce.inventoryservice.service;

import com.astracommerce.inventoryservice.dto.InventoryResponse;
import com.astracommerce.inventoryservice.entity.Inventory;
import com.astracommerce.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(Long productId) {
        return toResponse(findOrThrow(productId));
    }

    @Override
    public InventoryResponse initializeInventory(Long productId, int quantity) {
        if (inventoryRepository.existsByProductId(productId)) {
            throw new IllegalStateException("Inventory already exists for productId: " + productId);
        }
        return toResponse(inventoryRepository.save(Inventory.builder()
                .productId(productId).quantityAvailable(quantity).reservedQuantity(0).build()));
    }

    @Override
    public InventoryResponse decrementStock(Long productId, int quantity) {
        Inventory inv = findOrThrow(productId);
        if (inv.getQuantityAvailable() < quantity) {
            throw new IllegalStateException(
                    "Insufficient stock for productId=" + productId +
                    ". Available: " + inv.getQuantityAvailable() + ", Requested: " + quantity);
        }
        inv.setQuantityAvailable(inv.getQuantityAvailable() - quantity);
        log.info("Stock decremented for productId={}. New quantity={}", productId, inv.getQuantityAvailable());
        return toResponse(inventoryRepository.save(inv));
    }

    @Override
    public InventoryResponse incrementStock(Long productId, int quantity) {
        Inventory inv = findOrThrow(productId);
        inv.setQuantityAvailable(inv.getQuantityAvailable() + quantity);
        return toResponse(inventoryRepository.save(inv));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInStock(Long productId, int quantity) {
        return inventoryRepository.findByProductId(productId)
                .map(inv -> inv.getQuantityAvailable() >= quantity)
                .orElse(false);
    }

    private Inventory findOrThrow(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("No inventory found for productId: " + productId));
    }

    private InventoryResponse toResponse(Inventory inv) {
        return InventoryResponse.builder()
                .id(inv.getId()).productId(inv.getProductId())
                .quantityAvailable(inv.getQuantityAvailable())
                .reservedQuantity(inv.getReservedQuantity())
                .lastUpdated(inv.getLastUpdated()).build();
    }
}
