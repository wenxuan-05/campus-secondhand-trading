package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.ReviewDTO;
import com.campus.secondhand.entity.Review;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Result<Review> submit(@Valid @RequestBody ReviewDTO dto) {
        return Result.ok(reviewService.submitReview(UserContext.getUserId(), dto));
    }

    @GetMapping("/order/{orderId}")
    public Result<List<Review>> orderReviews(@PathVariable Long orderId) {
        return Result.ok(reviewService.getOrderReviews(orderId));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Review>> userReviews(@PathVariable Long userId) {
        return Result.ok(reviewService.getUserReviews(userId));
    }
}
