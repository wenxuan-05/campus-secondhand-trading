package com.campus.secondhand.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserProductVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private List<ProductItem> goods;
    private List<BuyRequestItem> buyRequests;

    @Data
    public static class ProductItem {
        private Long id;
        private String title;
        private String price;
        private String images;
        private Integer status;
        private String createdAt;
    }

    @Data
    public static class BuyRequestItem {
        private Long id;
        private String title;
        private String budget;
        private Integer status;
        private String createdAt;
    }
}
