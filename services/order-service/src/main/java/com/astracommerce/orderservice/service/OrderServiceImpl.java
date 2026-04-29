package com.astracommerce.orderservice.service;

import com.astracommerce.orderservice.dto.OrderRequest;
import com.astracommerce.orderservice.dto.OrderResponse;
import com.astracommerce.orderservice.entity.Order;
import com.astracommerce.orderservice.entity.OrderStatus;
import com.astracommerce.orderservice.event.OrderPlacedEvent;
import com.astracommerce.orderservice.exception.ResourceNotFoundException;
import com.astracommerce.orderservice.kafka.OrderEventProducer;
import com.astracommerce.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating order for userId={}, productId={}", request.getUserId(), request.getProductId());

        // NOTE: In a full impl, we'd call product-service via Feign to get price.
        // Using a placeholder price here — Feign client added in M3 when Gateway is up.
        BigDecimal unitPrice = BigDecimal.valueOf(100.00); // placeholder
        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = Order.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalPrice(total)
                .status(OrderStatus.PENDING)
                .build();

        Order saved = orderRepository.save(order);
        log.info("Order created with id: {}", saved.getId());

        // Publish event to Kafka → consumed by inventory-service and notification-service
        eventProducer.publishOrderPlaced(OrderPlacedEvent.builder()
                .orderId(saved.getId())
                .userId(saved.getUserId())
                .productId(saved.getProductId())
                .quantity(saved.getQuantity())
                .totalPrice(saved.getTotalPrice())
                .orderedAt(LocalDateTime.now())
                .build());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = findOrThrow(id);
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        return toResponse(orderRepository.save(order));
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = findOrThrow(id);
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Cannot cancel an order that is already " + order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Order {} cancelled", id);
    }

    private Order findOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    private OrderResponse toResponse(Order o) {
        return OrderResponse.builder()
                .id(o.getId()).userId(o.getUserId()).productId(o.getProductId())
                .quantity(o.getQuantity()).totalPrice(o.getTotalPrice())
                .status(o.getStatus()).createdAt(o.getCreatedAt()).build();
    }
}
