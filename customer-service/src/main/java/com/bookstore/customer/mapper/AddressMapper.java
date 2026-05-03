package com.bookstore.customer.mapper;

import com.bookstore.customer.dto.request.AddressRequest;
import com.bookstore.customer.dto.response.AddressResponse;
import com.bookstore.customer.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressRequest request, Long userId) {
        return Address.builder()
                .userId(userId)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .country(request.getCountry())
                .isDefault(false)
                .build();
    }

    public AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .phone(address.getPhone())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .isDefault(address.isDefault())
                .build();
    }
}