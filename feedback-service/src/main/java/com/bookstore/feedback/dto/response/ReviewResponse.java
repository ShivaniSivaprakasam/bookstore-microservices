package com.bookstore.feedback.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;
}