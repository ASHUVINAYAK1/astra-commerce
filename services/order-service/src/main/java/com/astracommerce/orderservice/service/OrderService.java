package com.astracommerce.orderservice.service;

import com.astracommerce.orderservice.dto.OrderRequest;
import com.astracommerce.orderservice.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByUser(Long userId);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(Long id, String status);
    void cancelOrder(Long id);
}
