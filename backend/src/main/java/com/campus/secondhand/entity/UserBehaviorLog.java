package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_behavior_log")
public class UserBehaviorLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long goodsId;
    /** 1=浏览, 2=收藏, 3=购买, 4=发起聊天, 5=搜索点击 */
    private Integer behaviorType;
    /** 行为权重（默认1.0，可扩展） */
    private Double weight;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
