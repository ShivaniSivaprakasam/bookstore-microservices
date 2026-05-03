package com.bookstore.customer.controller;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.customer.dto.request.AddressRequest;
import com.bookstore.customer.dto.request.CustomerProfileRequest;
import com.bookstore.customer.dto.response.AddressResponse;
import com.bookstore.customer.dto.response.CustomerProfileResponse;
import com.bookstore.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<CustomerProfileResponse>> getProfile(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(customerService.getProfile(userId));
    }

    @PostMapping("/details")
    public ResponseEntity<ApiResponse<CustomerProfileResponse>> createProfile(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CustomerProfileRequest request) {
        return ResponseEntity.ok(customerService.createProfile(userId, request));
    }

    @PutMapping("/details")
    public ResponseEntity<ApiResponse<CustomerProfileResponse>> updateProfile(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CustomerProfileRequest request) {
        return ResponseEntity.ok(customerService.updateProfile(userId, request));
    }

    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(customerService.addAddress(userId, request));
    }

    @GetMapping("/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(customerService.getAddresses(userId));
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.deleteAddress(userId, id));
    }

    @PutMapping("/addresses/{id}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.setDefaultAddress(userId, id));
    }
}