package com.bookstore.feedback.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponse {

    private Long productId;
    private Double averageRating;
    private Integer totalReviews;
}