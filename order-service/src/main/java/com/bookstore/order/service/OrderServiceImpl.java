package com.bookstore.order.service.impl;

import com.bookstore.common.exception.ResourceNotFoundException;
import com.bookstore.common.response.ApiResponse;
import com.bookstore.order.dto.request.OrderRequest;
import com.bookstore.order.dto.request.OrderStatusRequest;
import com.bookstore.order.dto.response.OrderResponse;
import com.bookstore.order.entity.Order;
import com.bookstore.order.entity.OrderItem;
import com.bookstore.order.entity.OrderStatus;
import com.bookstore.order.event.OrderEventPublisher;
import com.bookstore.order.feign.CartItemResponse;
import com.bookstore.order.feign.CartResponse;
import com.bookstore.order.feign.CartServiceClient;
import com.bookstore.order.mapper.OrderMapper;
import com.bookstore.order.repository.OrderRepository;
import com.bookstore.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartServiceClient cartServiceClient;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    public ApiResponse<OrderResponse> placeOrder(Long userId, String token, OrderRequest request) {
        CartResponse cart = cartServiceClient.getCart("Bearer " + token);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return ApiResponse.error("Cart is empty", 400);
        }

        List<OrderItem> items = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .productId(cartItem.getProductId())
                        .productTitle(cartItem.getProductTitle())
                        .quantity(cartItem.getQuantity())
                        .unitPrice(cartItem.getUnitPrice())
                        .totalPrice(cartItem.getUnitPrice()
                                .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(userId)
                .shippingAddress(request.getShippingAddress())
                .totalAmount(totalAmount)
                .build();

        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        Order saved = orderRepository.save(order);

        cartServiceClient.clearCart("Bearer " + token);

        orderEventPublisher.publishOrderPlaced(saved);

        return ApiResponse.created(orderMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<List<OrderResponse>> getUserOrders(Long userId) {
        List<OrderResponse> orders = orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(orders);
    }

    @Override
    public ApiResponse<OrderResponse> getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            return ApiResponse.error("Unauthorized", 403);
        }
        return ApiResponse.success(orderMapper.toResponse(order));
    }

    @Override
    public ApiResponse<Void> cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            return ApiResponse.error("Unauthorized", 403);
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            return ApiResponse.error("Only pending orders can be cancelled", 400);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        orderEventPublisher.publishOrderStatusChanged(order);
        return ApiResponse.success(null);
    }

    @Override
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(orders);
    }

    @Override
    public ApiResponse<OrderResponse> updateOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(OrderStatus.valueOf(request.getStatus()));
        Order updated = orderRepository.save(order);
        orderEventPublisher.publishOrderStatusChanged(updated);
        return ApiResponse.success(orderMapper.toResponse(updated));
    }
}
