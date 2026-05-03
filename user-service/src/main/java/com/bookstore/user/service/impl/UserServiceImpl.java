package com.bookstore.user.service.impl;

import com.bookstore.common.exception.ResourceNotFoundException;
import com.bookstore.common.response.ApiResponse;
import com.bookstore.user.dto.request.ChangePasswordRequest;
import com.bookstore.user.dto.request.LoginRequest;
import com.bookstore.user.dto.request.RegisterRequest;
import com.bookstore.user.dto.request.UpdateProfileRequest;
import com.bookstore.user.dto.response.AuthResponse;
import com.bookstore.user.dto.response.UserResponse;
import com.bookstore.user.entity.Role;
import com.bookstore.user.entity.User;
import com.bookstore.user.mapper.UserMapper;
import com.bookstore.user.repository.UserRepository;
import com.bookstore.user.security.JwtUtil;
import com.bookstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("Email already exists", 400);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .user(userMapper.toResponse(saved))
                .build();

        return ApiResponse.created(authResponse);
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponse.error("Invalid credentials", 401);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .user(userMapper.toResponse(user))
                .build();

        return ApiResponse.success(authResponse);
    }

    @Override
    public ApiResponse<UserResponse> getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ApiResponse.success(userMapper.toResponse(user));
    }

    @Override
    public ApiResponse<UserResponse> updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(request.getName());
        User updated = userRepository.save(user);

        return ApiResponse.success(userMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<Void> changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ApiResponse.error("Old password is incorrect", 400);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ApiResponse.success(null);
    }

    @Override
    public ApiResponse<Void> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
        return ApiResponse.success(null);
    }
}