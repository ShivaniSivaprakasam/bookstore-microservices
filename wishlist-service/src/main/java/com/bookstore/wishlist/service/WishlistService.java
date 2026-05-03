package com.bookstore.wishlist.service;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.wishlist.dto.WishlistResponse;

public interface WishlistService {

    ApiResponse<WishlistResponse> getWishlist(String userId);

    ApiResponse<WishlistResponse> addToWishlist(String userId, Long productId);

    ApiResponse<WishlistResponse> removeFromWishlist(String userId, Long productId);

    ApiResponse<Void> clearWishlist(String userId);
}