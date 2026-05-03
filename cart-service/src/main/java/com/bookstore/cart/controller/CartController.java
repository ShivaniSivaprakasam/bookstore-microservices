package com.bookstore.cart.controller;

import com.bookstore.cart.dto.request.AddToCartRequest;
import com.bookstore.cart.dto.request.UpdateCartRequest;
import com.bookstore.cart.dto.response.CartResponse;
import com.bookstore.cart.service.CartService;
import com.bookstore.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartResponse>> updateCart(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody UpdateCartRequest request) {
        return ResponseEntity.ok(cartService.updateCart(userId, request));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            @AuthenticationPrincipal String userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<CartResponse>> getCartTotal(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(cartService.getCartTotal(userId));
    }
}