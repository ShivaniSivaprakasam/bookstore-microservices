package com.bookstore.admin.service.impl;

import com.bookstore.admin.dto.request.AdminLoginRequest;
import com.bookstore.admin.dto.request.AdminRegisterRequest;
import com.bookstore.admin.dto.response.AdminAuthResponse;
import com.bookstore.admin.dto.response.AdminResponse;
import com.bookstore.admin.entity.Admin;
import com.bookstore.admin.entity.AdminRole;
import com.bookstore.admin.figen.ProductServiceClient;
import com.bookstore.admin.mapper.AdminMapper;
import com.bookstore.admin.repository.AdminRepository;
import com.bookstore.admin.security.JwtUtil;
import com.bookstore.admin.service.AdminService;
import com.bookstore.common.exception.ResourceNotFoundException;
import com.bookstore.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ProductServiceClient productServiceClient;

    @Override
    public ApiResponse<AdminAuthResponse> register(AdminRegisterRequest request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("Email already exists", 400);
        }

        Admin admin = Admin.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AdminRole.ADMIN)
                .build();

        Admin saved = adminRepository.save(admin);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());

        AdminAuthResponse authResponse = AdminAuthResponse.builder()
                .token(token)
                .admin(adminMapper.toResponse(saved))
                .build();

        return ApiResponse.created(authResponse);
    }

    @Override
    public ApiResponse<AdminAuthResponse> login(AdminLoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return ApiResponse.error("Invalid credentials", 401);
        }

        String token = jwtUtil.generateToken(admin.getEmail(), admin.getRole().name());

        AdminAuthResponse authResponse = AdminAuthResponse.builder()
                .token(token)
                .admin(adminMapper.toResponse(admin))
                .build();

        return ApiResponse.success(authResponse);
    }

    @Override
    public ApiResponse<List<AdminResponse>> getAllAdmins() {
        List<AdminResponse> admins = adminRepository.findAll()
                .stream()
                .map(adminMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.success(admins);
    }

    @Override
    public ApiResponse<Object> updateProduct(Long id, Map<String, Object> request, String token) {
        return productServiceClient.updateProduct(id, request, token);
    }

    @Override
    public ApiResponse<Object> deleteProduct(Long id, String token) {
        return productServiceClient.deleteProduct(id, token);
    }
}