package com.bookstore.customer.service;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.customer.dto.request.AddressRequest;
import com.bookstore.customer.dto.request.CustomerProfileRequest;
import com.bookstore.customer.dto.response.AddressResponse;
import com.bookstore.customer.dto.response.CustomerProfileResponse;

import java.util.List;

public interface CustomerService {

    ApiResponse<CustomerProfileResponse> getProfile(Long userId);
    ApiResponse<CustomerProfileResponse> createProfile(Long userId, CustomerProfileRequest request);
    ApiResponse<CustomerProfileResponse> updateProfile(Long userId, CustomerProfileRequest request);

    ApiResponse<AddressResponse> addAddress(Long userId, AddressRequest request);
    ApiResponse<List<AddressResponse>> getAddresses(Long userId);
    ApiResponse<Void> deleteAddress(Long userId, Long addressId);
    ApiResponse<AddressResponse> setDefaultAddress(Long userId, Long addressId);
}