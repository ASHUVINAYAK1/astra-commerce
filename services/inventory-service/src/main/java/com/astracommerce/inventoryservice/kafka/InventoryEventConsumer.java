package com.astracommerce.inventoryservice.kafka;

import com.astracommerce.inventoryservice.event.OrderPlacedEvent;
import com.astracommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for inventory-service.
 * Listens to the "order-placed" topic and decrements stock for the ordered product.
 *
 * This is the core of the Event-Driven Architecture (Milestone 5).
 *
 * Flow:
 *   order-service → Kafka[order-placed] → InventoryEventConsumer → InventoryService.decrementStock()
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "order-placed",
            groupId = "inventory-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Received order-placed event: orderId={}, productId={}, quantity={}",
                event.getOrderId(), event.getProductId(), event.getQuantity());
        try {
            inventoryService.decrementStock(event.getProductId(), event.getQuantity());
            log.info("Inventory updated for productId={}, decremented by {}",
                    event.getProductId(), event.getQuantity());
        } catch (Exception e) {
            log.error("Failed to update inventory for orderId={}: {}",
                    event.getOrderId(), e.getMessage());
            // TODO (M8): Send to DLQ (Dead Letter Queue) for retry
        }
    }
}
