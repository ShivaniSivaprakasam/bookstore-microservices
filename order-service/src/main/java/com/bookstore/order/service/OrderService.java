package com.bookstore.order.service;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.order.dto.request.OrderRequest;
import com.bookstore.order.dto.request.OrderStatusRequest;
import com.bookstore.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    ApiResponse<OrderResponse> placeOrder(Long userId, String token, OrderRequest request);
    ApiResponse<List<OrderResponse>> getUserOrders(Long userId);
    ApiResponse<OrderResponse> getOrderById(Long userId, Long orderId);
    ApiResponse<Void> cancelOrder(Long userId, Long orderId);
    ApiResponse<List<OrderResponse>> getAllOrders();
    ApiResponse<OrderResponse> updateOrderStatus(Long orderId, OrderStatusRequest request);
}
