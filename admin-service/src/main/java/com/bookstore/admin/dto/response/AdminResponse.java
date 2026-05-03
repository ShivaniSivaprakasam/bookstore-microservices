package com.bookstore.admin.dto.response;

import com.bookstore.admin.entity.AdminRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminResponse {

    private Long id;
    private String name;
    private String email;
    private AdminRole role;
    private LocalDateTime createdAt;
}