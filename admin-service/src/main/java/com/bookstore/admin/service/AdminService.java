package com.bookstore.admin.service;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.admin.dto.request.AdminLoginRequest;
import com.bookstore.admin.dto.request.AdminRegisterRequest;
import com.bookstore.admin.dto.response.AdminAuthResponse;
import com.bookstore.admin.dto.response.AdminResponse;

import java.util.List;
import java.util.Map;

public interface AdminService {

    ApiResponse<AdminAuthResponse> register(AdminRegisterRequest request);

    ApiResponse<AdminAuthResponse> login(AdminLoginRequest request);

    ApiResponse<List<AdminResponse>> getAllAdmins();

    ApiResponse<Object> updateProduct(Long id, Map<String, Object> request, String token);

    ApiResponse<Object> deleteProduct(Long id, String token);
}