package com.bookstore.feedback.service.impl;

import com.bookstore.common.exception.ResourceNotFoundException;
import com.bookstore.common.response.ApiResponse;
import com.bookstore.feedback.dto.request.ReviewRequest;
import com.bookstore.feedback.dto.request.ReviewUpdateRequest;
import com.bookstore.feedback.dto.response.RatingResponse;
import com.bookstore.feedback.dto.response.ReviewResponse;
import com.bookstore.feedback.entity.Review;
import com.bookstore.feedback.mapper.ReviewMapper;
import com.bookstore.feedback.repository.ReviewRepository;
import com.bookstore.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ApiResponse<ReviewResponse> submitReview(Long userId, ReviewRequest request) {
        if (reviewRepository.findByUserIdAndProductId(userId, request.getProductId()).isPresent()) {
            return ApiResponse.error("You have already reviewed this product", 400);
        }
        Review review = reviewMapper.toEntity(request, userId);
        Review saved = reviewRepository.save(review);
        return ApiResponse.created(reviewMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<List<ReviewResponse>> getReviewsByProduct(Long productId) {
        List<ReviewResponse> reviews = reviewRepository.findByProductId(productId)
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(reviews);
    }

    @Override
    public ApiResponse<RatingResponse> getAverageRating(Long productId) {
        Double average = reviewRepository.findAverageRatingByProductId(productId);
        int totalReviews = reviewRepository.findByProductId(productId).size();
        RatingResponse response = RatingResponse.builder()
                .productId(productId)
                .averageRating(average != null ? average : 0.0)
                .totalReviews(totalReviews)
                .build();
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<ReviewResponse> updateReview(Long userId, Long reviewId,
                                                    ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getUserId().equals(userId)) {
            return ApiResponse.error("Unauthorized", 403);
        }
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        Review updated = reviewRepository.save(review);
        return ApiResponse.success(reviewMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<Void> deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getUserId().equals(userId)) {
            return ApiResponse.error("Unauthorized", 403);
        }
        reviewRepository.delete(review);
        return ApiResponse.success(null);
    }
}