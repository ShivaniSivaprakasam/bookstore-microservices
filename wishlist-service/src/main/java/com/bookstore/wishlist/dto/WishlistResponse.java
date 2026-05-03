package com.bookstore.wishlist.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WishlistResponse {

    private String userId;
    private List<WishlistItemResponse> items;
    private int totalItems;
}