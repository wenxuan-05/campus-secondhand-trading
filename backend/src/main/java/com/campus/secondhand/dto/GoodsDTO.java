package com.campus.secondhand.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsDTO {
    @NotBlank(message = "标题不能为空")
    private String title;
    private String description;
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer conditionLevel;
    private String category;
    private String location;
    private List<String> images;
}
