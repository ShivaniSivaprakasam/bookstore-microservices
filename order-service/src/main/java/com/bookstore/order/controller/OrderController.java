package com.bookstore.order.controller;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.order.dto.request.OrderRequest;
import com.bookstore.order.dto.request.OrderStatusRequest;
import com.bookstore.order.dto.response.OrderResponse;
import com.bookstore.order.security.JwtUtil;
import com.bookstore.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @AuthenticationPrincipal String email,
            HttpServletRequest httpRequest,
            @Valid @RequestBody OrderRequest request) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(orderService.placeOrder(userId, token, request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders(
            HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            HttpServletRequest httpRequest,
            @PathVariable Long id) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(orderService.getOrderById(userId, id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            HttpServletRequest httpRequest,
            @PathVariable Long id) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(orderService.cancelOrder(userId, id));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }
}