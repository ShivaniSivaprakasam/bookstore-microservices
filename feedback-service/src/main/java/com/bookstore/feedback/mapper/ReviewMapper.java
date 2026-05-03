package com.bookstore.feedback.mapper;

import com.bookstore.feedback.dto.request.ReviewRequest;
import com.bookstore.feedback.dto.response.ReviewResponse;
import com.bookstore.feedback.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequest request, Long userId) {
        return Review.builder()
                .userId(userId)
                .productId(request.getProductId())
                .comment(request.getComment())
                .rating(request.getRating())
                .build();
    }

    public ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .productId(review.getProductId())
                .comment(review.getComment())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .build();
    }
}