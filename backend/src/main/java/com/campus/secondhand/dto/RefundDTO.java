package com.campus.secondhand.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class RefundDTO {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotBlank(message = "退款原因不能为空")
    private String reason;

    private List<String> images;    // 举证图片
}
