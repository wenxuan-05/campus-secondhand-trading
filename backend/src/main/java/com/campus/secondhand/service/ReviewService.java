package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.entity.Review;
import com.campus.secondhand.dto.ReviewDTO;

import java.util.List;

public interface ReviewService extends IService<Review> {
    /** Submit a review after transaction completes */
    Review submitReview(Long reviewerId, ReviewDTO dto);
    /** Get reviews for a specific order */
    List<Review> getOrderReviews(Long orderId);
    /** Get recent reviews for a user (last 10) */
    List<Review> getUserReviews(Long userId);
    /** Auto-generate 5-star reviews for expired un-reviewed orders */
    int autoReviewExpiredOrders();
}
