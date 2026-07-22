package com.campus.secondhand.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportVO {
    private Long id;
    private Long reporterId;
    private String reporterName;
    private Long reportedUserId;
    private String reportedUserName;
    private Long messageId;
    private String sessionId;
    private String reportType;      // 'message' or 'user'
    private String messageContent;
    private String reason;
    private String description;
    private Integer status;        // 0=pending, 1=reviewed, 2=dismissed
    private String statusLabel;    // 待处理/已处理/已驳回
    private String handlerNote;
    private Long handledBy;
    private LocalDateTime handledAt;
    private LocalDateTime createdAt;
}
