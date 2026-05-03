package com.bookstore.wishlist.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WishlistItemResponse {

    private Long id;
    private Long productId;
    private String productTitle;
    private String author;
    private String imageUrl;
    private LocalDateTime addedAt;
}