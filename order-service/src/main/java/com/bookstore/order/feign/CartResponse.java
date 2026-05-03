package com.bookstore.order.feign;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private String userId;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
}