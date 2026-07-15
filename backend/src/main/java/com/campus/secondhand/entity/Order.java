package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long buyerId;
    private Long sellerId;
    private Long goodsId;
    private BigDecimal price;
    // 10=待付款, 20=待面交, 25=待发货, 30=待确认, 40=待评价
    // 50=已评价, 60=退款中, 70=退款纠纷, 80=已退款, 90=已取消, 100=已关闭
    private Integer status;
    private String pickupCode;
    private String remark;
    private String cancelReason;      // 取消原因
    private LocalDateTime payTime;    // 支付时间
    private LocalDateTime pickupTime; // 面交时间
    private LocalDateTime confirmTime;// 确认收货时间
    private LocalDateTime cancelTime; // 取消时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
