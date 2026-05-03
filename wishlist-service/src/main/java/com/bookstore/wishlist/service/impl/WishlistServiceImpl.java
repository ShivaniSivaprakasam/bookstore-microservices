package com.bookstore.wishlist.service.impl;

import com.bookstore.common.exception.ResourceNotFoundException;
import com.bookstore.common.response.ApiResponse;
import com.bookstore.wishlist.dto.WishlistResponse;
import com.bookstore.wishlist.entity.WishlistItem;
import com.bookstore.wishlist.feign.ProductServiceClient;
import com.bookstore.wishlist.mapper.WishlistMapper;
import com.bookstore.wishlist.repository.WishlistRepository;
import com.bookstore.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;
    private final ProductServiceClient productServiceClient;

    @Override
    public ApiResponse<WishlistResponse> getWishlist(String userId) {
        List<WishlistItem> items = wishlistRepository.findByUserId(userId);

        WishlistResponse response = WishlistResponse.builder()
                .userId(userId)
                .items(items.stream()
                        .map(wishlistMapper::toResponse)
                        .collect(Collectors.toList()))
                .totalItems(items.size())
                .build();

        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<WishlistResponse> addToWishlist(String userId, Long productId) {
        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            return ApiResponse.error("Product already in wishlist", 400);
        }

        ApiResponse<Map<String, Object>> productResponse =
                productServiceClient.getProductById(productId);

        if (productResponse == null || productResponse.getData() == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        Map<String, Object> product = productResponse.getData();

        WishlistItem item = WishlistItem.builder()
                .userId(userId)
                .productId(productId)
                .productTitle((String) product.get("title"))
                .author((String) product.get("author"))
                .imageUrl((String) product.get("imageUrl"))
                .build();

        wishlistRepository.save(item);

        return getWishlist(userId);
    }

    @Override
    @Transactional
    public ApiResponse<WishlistResponse> removeFromWishlist(String userId, Long productId) {
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
        return getWishlist(userId);
    }

    @Override
    @Transactional
    public ApiResponse<Void> clearWishlist(String userId) {
        wishlistRepository.deleteAllByUserId(userId);
        return ApiResponse.success(null);
    }
}