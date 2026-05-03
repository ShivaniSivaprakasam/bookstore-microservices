package com.bookstore.admin.mapper;

import com.bookstore.admin.dto.response.AdminResponse;
import com.bookstore.admin.entity.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public AdminResponse toResponse(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .role(admin.getRole())
                .createdAt(admin.getCreatedAt())
                .build();
    }
}
