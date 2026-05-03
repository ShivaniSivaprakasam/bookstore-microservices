package com.bookstore.order.feign;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String productTitle;
    private Integer quantity;
    private BigDecimal unitPrice;
}