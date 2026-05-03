package com.bookstore.customer.service.impl;

import com.bookstore.common.exception.ResourceNotFoundException;
import com.bookstore.common.response.ApiResponse;
import com.bookstore.customer.dto.request.AddressRequest;
import com.bookstore.customer.dto.request.CustomerProfileRequest;
import com.bookstore.customer.dto.response.AddressResponse;
import com.bookstore.customer.dto.response.CustomerProfileResponse;
import com.bookstore.customer.entity.Address;
import com.bookstore.customer.entity.CustomerProfile;
import com.bookstore.customer.mapper.AddressMapper;
import com.bookstore.customer.mapper.CustomerProfileMapper;
import com.bookstore.customer.repository.AddressRepository;
import com.bookstore.customer.repository.CustomerProfileRepository;
import com.bookstore.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final CustomerProfileMapper profileMapper;
    private final AddressMapper addressMapper;

    @Override
    public ApiResponse<CustomerProfileResponse> getProfile(Long userId) {
        CustomerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return ApiResponse.success(profileMapper.toResponse(profile));
    }

    @Override
    public ApiResponse<CustomerProfileResponse> createProfile(Long userId, CustomerProfileRequest request) {
        if (profileRepository.existsByUserId(userId)) {
            return ApiResponse.error("Profile already exists", 400);
        }
        CustomerProfile profile = profileMapper.toEntity(request, userId);
        CustomerProfile saved = profileRepository.save(profile);
        return ApiResponse.created(profileMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<CustomerProfileResponse> updateProfile(Long userId, CustomerProfileRequest request) {
        CustomerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        profile.setPhone(request.getPhone());
        profile.setPreferences(request.getPreferences());
        CustomerProfile updated = profileRepository.save(profile);
        return ApiResponse.success(profileMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<AddressResponse> addAddress(Long userId, AddressRequest request) {
        Address address = addressMapper.toEntity(request, userId);
        Address saved = addressRepository.save(address);
        return ApiResponse.created(addressMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<List<AddressResponse>> getAddresses(Long userId) {
        List<AddressResponse> addresses = addressRepository.findByUserId(userId)
                .stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(addresses);
    }

    @Override
    public ApiResponse<Void> deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if (!address.getUserId().equals(userId)) {
            return ApiResponse.error("Unauthorized", 403);
        }
        addressRepository.delete(address);
        return ApiResponse.success(null);
    }

    @Override
    public ApiResponse<AddressResponse> setDefaultAddress(Long userId, Long addressId) {
        addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(a -> { a.setDefault(false); addressRepository.save(a); });
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        address.setDefault(true);
        Address updated = addressRepository.save(address);
        return ApiResponse.success(addressMapper.toResponse(updated));
    }
}