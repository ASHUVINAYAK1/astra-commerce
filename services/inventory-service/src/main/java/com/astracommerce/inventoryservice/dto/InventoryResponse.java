package com.astracommerce.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Integer quantityAvailable;
    private Integer reservedQuantity;
    private LocalDateTime lastUpdated;
}
