package com.campus.secondhand.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionVO {
    private String sessionId;
    private Long otherUserId;
    private String otherUserName;
    private String lastMessage;
    private LocalDateTime lastTime;
    private Integer unreadCount;
    private String otherUserAvatar; // other user's avatar URL
    private Long goodsId;          // parsed from sessionId (goods_ sessions)
    private String goodsTitle;     // related goods title for display
}
