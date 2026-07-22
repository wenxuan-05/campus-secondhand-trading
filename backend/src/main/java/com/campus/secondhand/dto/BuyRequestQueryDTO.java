package com.campus.secondhand.dto;

import lombok.Data;

@Data
public class BuyRequestQueryDTO {
    private String keyword;
    private String category;
    private Integer status;
    private Integer page = 1;
    private Integer pageSize = 20;
}
