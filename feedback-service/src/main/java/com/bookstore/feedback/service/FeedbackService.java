package com.bookstore.feedback.service;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.feedback.dto.request.ReviewRequest;
import com.bookstore.feedback.dto.request.ReviewUpdateRequest;
import com.bookstore.feedback.dto.response.RatingResponse;
import com.bookstore.feedback.dto.response.ReviewResponse;

import java.util.List;

public interface FeedbackService {

    ApiResponse<ReviewResponse> submitReview(Long userId, ReviewRequest request);
    ApiResponse<List<ReviewResponse>> getReviewsByProduct(Long productId);
    ApiResponse<RatingResponse> getAverageRating(Long productId);
    ApiResponse<ReviewResponse> updateReview(Long userId, Long reviewId, ReviewUpdateRequest request);
    ApiResponse<Void> deleteReview(Long userId, Long reviewId);
}