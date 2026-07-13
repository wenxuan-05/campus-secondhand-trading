package com.campustrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userNo;
    private String phone;
    private String password;
    private String nickname;
    private String avatar;
    private Integer status;
    private Integer creditScore;
    private Integer creditLevel;
    private Integer tradeCount;
    private Double goodRate;
    private LocalDateTime lastLoginTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
