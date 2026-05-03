package com.bookstore.wishlist.mapper;

import com.bookstore.wishlist.dto.WishlistItemResponse;
import com.bookstore.wishlist.entity.WishlistItem;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistItemResponse toResponse(WishlistItem item) {
        return WishlistItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productTitle(item.getProductTitle())
                .author(item.getAuthor())
                .imageUrl(item.getImageUrl())
                .addedAt(item.getAddedAt())
                .build();
    }
}