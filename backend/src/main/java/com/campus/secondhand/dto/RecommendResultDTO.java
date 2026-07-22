package com.campus.secondhand.dto;

import com.campus.secondhand.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendResultDTO {
    private Goods goods;
    private Double score;
    /** 推荐理由（展示给用户的中文文案） */
    private String reason;
    /** 理由类型：category_match / location_proximity / popular / fresh / price_match */
    private String reasonType;
}
