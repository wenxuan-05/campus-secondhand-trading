package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("chat_messages")
public class ChatMessage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String sessionId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Integer type;         // 1=text, 2=image, 3=product_card, 4=bid
    private String extraData;     // JSON: product card / bid / image metadata
    private Integer isRead;       // 0=unread, 1=read
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
