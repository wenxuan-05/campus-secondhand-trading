package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("reports")
public class Report {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long reporterId;
    private Long reportedUserId;
    private Long messageId;
    private String sessionId;
    private String reportType;     // 'message' or 'user'
    private String reason;         // harassment/spam/fraud/other
    private String description;
    private Integer status;        // 0=pending, 1=reviewed, 2=dismissed
    private String handlerNote;
    private Long handledBy;
    private LocalDateTime handledAt;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
