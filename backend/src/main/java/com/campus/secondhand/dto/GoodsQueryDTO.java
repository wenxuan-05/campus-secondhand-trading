package com.campus.secondhand.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GoodsQueryDTO {
    private String keyword;
    private String category;
    private Integer conditionLevel;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private Integer status;
    private String sortBy;        // price_asc, price_desc, created_desc, view_desc, location_desc, personalized
    private String dormitory;     // current user's dormitory for location-based sorting
    private Integer page = 1;
    private Integer pageSize = 20;
}
