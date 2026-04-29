package com.astracommerce.orderservice.kafka;

import com.astracommerce.orderservice.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka producer for Order Service.
 * Publishes OrderPlacedEvent to the "order-placed" topic after order creation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    public static final String ORDER_PLACED_TOPIC = "order-placed";

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    /**
     * Publish an order-placed event to Kafka.
     * Key = orderId (string) for partition routing.
     */
    public void publishOrderPlaced(OrderPlacedEvent event) {
        log.info("Publishing order-placed event for orderId: {}", event.getOrderId());

        CompletableFuture<SendResult<String, OrderPlacedEvent>> future =
                kafkaTemplate.send(ORDER_PLACED_TOPIC, event.getOrderId().toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish order-placed event for orderId: {}. Error: {}",
                        event.getOrderId(), ex.getMessage());
            } else {
                log.info("Order-placed event published successfully. orderId={}, partition={}, offset={}",
                        event.getOrderId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}
