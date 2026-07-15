package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.ReviewDTO;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.Review;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.OrderMapper;
import com.campus.secondhand.mapper.ReviewMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.service.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Review submitReview(Long reviewerId, ReviewDTO dto) {
        Order order = orderMapper.selectById(dto.getOrderId());
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getStatus() != 40) throw new BusinessException("当前状态不可评价");

        // Determine reviewer/reviewee
        Long revieweeId;
        int reviewType;
        if (reviewerId.equals(order.getBuyerId())) {
            revieweeId = order.getSellerId();
            reviewType = 1; // buyer→seller
        } else if (reviewerId.equals(order.getSellerId())) {
            revieweeId = order.getBuyerId();
            reviewType = 2; // seller→buyer
        } else {
            throw new BusinessException("无权评价此订单");
        }

        // Check duplicate
        long count = count(new LambdaQueryWrapper<Review>()
                .eq(Review::getOrderId, dto.getOrderId())
                .eq(Review::getReviewerId, reviewerId));
        if (count > 0) throw new BusinessException("已评价过此订单");

        // Create review
        Review review = new Review();
        review.setOrderId(dto.getOrderId());
        review.setReviewerId(reviewerId);
        review.setRevieweeId(revieweeId);
        review.setRating(dto.getRating());
        review.setContent(dto.getContent() != null ? dto.getContent() : "");
        review.setReviewType(reviewType);
        review.setIsAuto(0);

        try {
            review.setTags(dto.getTags() != null ? objectMapper.writeValueAsString(dto.getTags()) : "[]");
            review.setImages(dto.getImages() != null ? objectMapper.writeValueAsString(dto.getImages()) : "[]");
        } catch (JsonProcessingException e) {
            review.setTags("[]");
            review.setImages("[]");
        }
        save(review);

        // Update reviewee credit score
        updateCreditScore(revieweeId, dto);

        // Check if both sides have reviewed → close order
        long reviewCount = count(new LambdaQueryWrapper<Review>()
                .eq(Review::getOrderId, dto.getOrderId()));
        if (reviewCount >= 2) {
            order.setStatus(50); // 已评价
            orderMapper.updateById(order);
        }

        return review;
    }

    private void updateCreditScore(Long userId, ReviewDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) return;

        int scoreChange = 0;
        if (dto.getRating() >= 4) {
            scoreChange = 2; // 好评 +2
        } else if (dto.getRating() <= 2) {
            scoreChange = -5; // 差评 -5
        }
        // 中评(3星) 不变

        // Extra point for image review
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            scoreChange += 1;
        }

        int newScore = Math.max(0, user.getCreditScore() + scoreChange);
        user.setCreditScore(newScore);

        // Increment trade count
        if (user.getTradeCount() == null) user.setTradeCount(0);
        user.setTradeCount(user.getTradeCount() + 1);

        // Recalculate good rate
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<Review>()
                .eq(Review::getRevieweeId, userId);
        long totalReviews = count(wrapper);
        long goodReviews = count(wrapper.ge(Review::getRating, 4));
        if (totalReviews > 0) {
            BigDecimal rate = BigDecimal.valueOf(goodReviews * 100.0 / totalReviews)
                    .setScale(1, RoundingMode.HALF_UP);
            user.setGoodRate(rate);
        }

        // Update credit level
        user.setCreditLevel(calculateCreditLevel(newScore));
        userMapper.updateById(user);
    }

    @Override
    public List<Review> getOrderReviews(Long orderId) {
        return list(new LambdaQueryWrapper<Review>()
                .eq(Review::getOrderId, orderId)
                .orderByDesc(Review::getCreatedAt));
    }

    @Override
    public List<Review> getUserReviews(Long userId) {
        Page<Review> p = new Page<>(1, 10);
        return page(p, new LambdaQueryWrapper<Review>()
                .eq(Review::getRevieweeId, userId)
                .orderByDesc(Review::getCreatedAt)).getRecords();
    }

    @Override
    @Transactional
    public int autoReviewExpiredOrders() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        // Find completed orders (status=40) older than 7 days with no reviews
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, 40)
                .le(Order::getConfirmTime, sevenDaysAgo));

        int count = 0;
        for (Order order : orders) {
            long reviewCount = count(new LambdaQueryWrapper<Review>()
                    .eq(Review::getOrderId, order.getId()));
            if (reviewCount < 2) {
                // Auto 5-star for buyer if not reviewed
                if (count(new LambdaQueryWrapper<Review>()
                        .eq(Review::getOrderId, order.getId())
                        .eq(Review::getReviewerId, order.getBuyerId())) == 0) {
                    createAutoReview(order.getId(), order.getBuyerId(), order.getSellerId(), 1);
                    count++;
                }
                // Auto 5-star for seller if not reviewed
                if (count(new LambdaQueryWrapper<Review>()
                        .eq(Review::getOrderId, order.getId())
                        .eq(Review::getReviewerId, order.getSellerId())) == 0) {
                    createAutoReview(order.getId(), order.getSellerId(), order.getBuyerId(), 2);
                    count++;
                }
                // Mark order as fully reviewed
                order.setStatus(50);
                orderMapper.updateById(order);
            }
        }
        return count;
    }

    private void createAutoReview(Long orderId, Long reviewerId, Long revieweeId, int reviewType) {
        Review review = new Review();
        review.setOrderId(orderId);
        review.setReviewerId(reviewerId);
        review.setRevieweeId(revieweeId);
        review.setRating(5);
        review.setContent("系统自动好评");
        review.setTags("[]");
        review.setImages("[]");
        review.setIsAuto(1);
        review.setReviewType(reviewType);
        save(review);

        // Update credit score: +2 for good review
        User user = userMapper.selectById(revieweeId);
        if (user != null) {
            user.setCreditScore(user.getCreditScore() + 2);
            if (user.getTradeCount() == null) user.setTradeCount(0);
            user.setTradeCount(user.getTradeCount() + 1);
            user.setCreditLevel(calculateCreditLevel(user.getCreditScore()));
            userMapper.updateById(user);
        }
    }

    private String calculateCreditLevel(int score) {
        if (score >= 90) return "优秀";
        if (score >= 70) return "良好";
        if (score >= 50) return "一般";
        if (score >= 30) return "较差";
        return "极差";
    }
}
