package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("reviews")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;           // 关联订单
    private Long reviewerId;        // 评价人
    private Long revieweeId;        // 被评价人
    private Integer rating;         // 1-5星
    private String tags;            // 标签（JSON数组）
    private String content;         // 文字评价（0-200字）
    private String images;          // 晒图（JSON数组）
    private Integer isAuto;         // 0=手动, 1=超时自动好评
    private Integer reviewType;     // 1=买家评卖家, 2=卖家评买家
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
