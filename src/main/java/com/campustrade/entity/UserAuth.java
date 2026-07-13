package com.campustrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_auth")
public class UserAuth {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Integer authType;
    private String authId;
    private String authName;
    private String authInfo;
    private LocalDateTime verifiedAt;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}