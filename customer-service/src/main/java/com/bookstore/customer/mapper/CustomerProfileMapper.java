package com.bookstore.customer.mapper;

import com.bookstore.customer.dto.request.CustomerProfileRequest;
import com.bookstore.customer.dto.response.CustomerProfileResponse;
import com.bookstore.customer.entity.CustomerProfile;
import org.springframework.stereotype.Component;

@Component
public class CustomerProfileMapper {

    public CustomerProfile toEntity(CustomerProfileRequest request, Long userId) {
        return CustomerProfile.builder()
                .userId(userId)
                .phone(request.getPhone())
                .preferences(request.getPreferences())
                .build();
    }

    public CustomerProfileResponse toResponse(CustomerProfile profile) {
        return CustomerProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .phone(profile.getPhone())
                .preferences(profile.getPreferences())
                .build();
    }
}