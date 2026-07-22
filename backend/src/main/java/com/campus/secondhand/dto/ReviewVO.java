package com.campus.secondhand.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewVO {
    private Long id;
    private Long orderId;
    private Long reviewerId;
    private String reviewerName;       // 评价人昵称
    private String reviewerAvatar;     // 评价人头像
    private Integer rating;            // 1-5星
    private List<String> tags;         // 标签
    private String content;            // 文字评价
    private List<String> images;       // 晒图
    private Integer isAuto;            // 0=手动, 1=超时自动好评
    private Integer reviewType;        // 1=买家评卖家, 2=卖家评买家
    private LocalDateTime createdAt;
}
