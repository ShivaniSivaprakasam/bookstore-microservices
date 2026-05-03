package com.bookstore.cart.service;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.cart.dto.request.AddToCartRequest;
import com.bookstore.cart.dto.request.UpdateCartRequest;
import com.bookstore.cart.dto.response.CartResponse;

public interface CartService {

    ApiResponse<CartResponse> getCart(String userId);

    ApiResponse<CartResponse> addToCart(String userId, AddToCartRequest request);

    ApiResponse<CartResponse> updateCart(String userId, UpdateCartRequest request);

    ApiResponse<CartResponse> removeFromCart(String userId, Long productId);

    ApiResponse<Void> clearCart(String userId);

    ApiResponse<CartResponse> getCartTotal(String userId);
}