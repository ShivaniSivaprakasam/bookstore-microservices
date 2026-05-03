package com.bookstore.order.mapper;

import com.bookstore.order.dto.response.OrderItemResponse;
import com.bookstore.order.dto.response.OrderResponse;
import com.bookstore.order.entity.Order;
import com.bookstore.order.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .items(mapItems(order.getItems()))
                .createdAt(order.getCreatedAt())
                .build();
    }

    private List<OrderItemResponse> mapItems(List<OrderItem> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productTitle(item.getProductTitle())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());
    }
}