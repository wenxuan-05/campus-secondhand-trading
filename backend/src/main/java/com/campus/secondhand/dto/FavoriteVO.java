package com.campus.secondhand.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FavoriteVO {
    private Long id;
    private Long userId;
    private Long goodsId;
    private LocalDateTime createdAt;

    // Goods fields for display
    private String goodsTitle;
    private String goodsImage;      // first image extracted from images JSON
    private BigDecimal goodsPrice;
    private Integer goodsStatus;
}
