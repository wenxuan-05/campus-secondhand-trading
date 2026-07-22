package com.campus.secondhand.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportDTO {
    private Long messageId;              // 消息举报时必填
    private Long reportedUserId;         // 用户举报时必填
    private String reportType;           // 'message' or 'user'

    @NotBlank(message = "举报原因不能为空")
    private String reason;         // harassment/spam/fraud/other

    private String description;    // optional details
}
