package com.bookstore.user.service;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.user.dto.request.ChangePasswordRequest;
import com.bookstore.user.dto.request.LoginRequest;
import com.bookstore.user.dto.request.RegisterRequest;
import com.bookstore.user.dto.request.UpdateProfileRequest;
import com.bookstore.user.dto.response.AuthResponse;
import com.bookstore.user.dto.response.UserResponse;

public interface UserService {

    ApiResponse<AuthResponse> register(RegisterRequest request);

    ApiResponse<AuthResponse> login(LoginRequest request);

    ApiResponse<UserResponse> getProfile(String email);

    ApiResponse<UserResponse> updateProfile(String email, UpdateProfileRequest request);

    ApiResponse<Void> changePassword(String email, ChangePasswordRequest request);

    ApiResponse<Void> deleteUser(Long id);
}