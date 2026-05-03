package com.bookstore.admin.controller;

import com.bookstore.admin.dto.request.AdminLoginRequest;
import com.bookstore.admin.dto.request.AdminRegisterRequest;
import com.bookstore.admin.dto.response.AdminAuthResponse;
import com.bookstore.admin.dto.response.AdminResponse;
import com.bookstore.admin.service.AdminService;
import com.bookstore.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AdminAuthResponse>> register(
            @Valid @RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(adminService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminAuthResponse>> login(
            @Valid @RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok(adminService.login(request));
    }

    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<List<AdminResponse>>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Object>> updateProduct(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(adminService.updateProduct(id, request, token));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(adminService.deleteProduct(id, token));
    }
}