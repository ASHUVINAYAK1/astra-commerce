package com.astracommerce.notificationservice.kafka;

import com.astracommerce.notificationservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Notification Service Kafka Consumer.
 * Listens to the "order-placed" topic and sends notifications.
 *
 * Currently logs structured notifications — can be extended to send
 * real emails via Spring Mail or SMS via Twilio in future iterations.
 */
@Component
@Slf4j
public class NotificationEventConsumer {

    @KafkaListener(
            topics = "order-placed",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("====== ORDER CONFIRMATION NOTIFICATION ======");
        log.info("  Order ID   : {}", event.getOrderId());
        log.info("  User ID    : {}", event.getUserId());
        log.info("  Product ID : {}", event.getProductId());
        log.info("  Quantity   : {}", event.getQuantity());
        log.info("  Total      : ${}", event.getTotalPrice());
        log.info("  Placed At  : {}", event.getOrderedAt());
        log.info("  Status     : CONFIRMED");
        log.info("=============================================");
        // TODO: Integrate Spring Mail for actual email notifications
    }
}
