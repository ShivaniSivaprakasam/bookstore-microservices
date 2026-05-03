package com.bookstore.cart.dto.response;

import com.bookstore.cart.model.CartItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {

    private String userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private int totalItems;
}