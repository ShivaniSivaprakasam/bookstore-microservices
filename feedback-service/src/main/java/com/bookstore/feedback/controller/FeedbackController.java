package com.bookstore.feedback.controller;

import com.bookstore.common.response.ApiResponse;
import com.bookstore.feedback.dto.request.ReviewRequest;
import com.bookstore.feedback.dto.request.ReviewUpdateRequest;
import com.bookstore.feedback.dto.response.RatingResponse;
import com.bookstore.feedback.dto.response.ReviewResponse;
import com.bookstore.feedback.security.JwtUtil;
import com.bookstore.feedback.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> submitReview(
            HttpServletRequest httpRequest,
            @Valid @RequestBody ReviewRequest request) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(feedbackService.submitReview(userId, request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(feedbackService.getReviewsByProduct(productId));
    }

    @GetMapping("/product/{productId}/rating")
    public ResponseEntity<ApiResponse<RatingResponse>> getAverageRating(
            @PathVariable Long productId) {
        return ResponseEntity.ok(feedbackService.getAverageRating(productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            HttpServletRequest httpRequest,
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateRequest request) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(feedbackService.updateReview(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            HttpServletRequest httpRequest,
            @PathVariable Long id) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(feedbackService.deleteReview(userId, id));
    }
}