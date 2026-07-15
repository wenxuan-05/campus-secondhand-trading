package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("refunds")
public class Refund {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long applicantId;       // 申请人
    private String reason;          // 退款原因
    private String images;          // 举证图片（JSON数组）
    private BigDecimal amount;      // 退款金额
    private Integer status;         // 1=待卖家处理, 2=卖家同意, 3=卖家拒绝, 4=平台仲裁, 5=退款完成, 6=驳回
    private String adminNote;       // 平台仲裁备注
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
