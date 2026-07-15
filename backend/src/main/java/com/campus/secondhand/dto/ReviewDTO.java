package com.campus.secondhand.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ReviewDTO {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer rating;

    private List<String> tags;      // 评价标签
    private String content;         // 文字评价（0-200字）
    private List<String> images;    // 晒图
    private Integer reviewType;     // 1=买家评卖家, 2=卖家评买家
}
