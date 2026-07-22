package com.campus.secondhand.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BuyRequestDTO {
    @NotBlank(message = "商品名称不能为空")
    private String title;

    @NotBlank(message = "商品分类不能为空")
    private String category;

    @NotNull(message = "预算价格不能为空")
    private BigDecimal budget;

    private String description;
}
