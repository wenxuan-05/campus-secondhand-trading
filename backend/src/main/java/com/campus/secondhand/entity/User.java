package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String studentId;
    private String schoolName;
    private String nickname;
    private String avatar;
    private String password;
    private String phone;
    private String realName;            // 真实姓名
    private String email;               // 邮箱
    private Integer gender;             // 0=未知, 1=男, 2=女
    private String dormitory;           // 宿舍楼
    private String roomNumber;          // 房间号
    private Integer creditScore;        // 信用分
    private String creditLevel;         // 信用等级：优秀/良好/一般/较差/极差
    private Integer tradeCount;         // 交易笔数
    private java.math.BigDecimal goodRate; // 好评率
    private LocalDateTime lastLoginTime;// 最后登录时间
    private Integer status;             // 0=disabled, 1=active, 2=限制发布
    private LocalDateTime banExpireTime; // 封禁到期时间（信用分<60时自动封禁7天）
    private String role;                // 角色：ADMIN, CAMPUS_AMBASSADOR, USER
    private String workerId;            // 工号（校园大使专用，管理员手动分配）
    private Integer isCampusAmbassador; // 0=否, 1=校园大使（已废弃，请使用role字段）
    private Integer isMerchant;         // 0=否, 1=商家（已废弃，请使用role字段）
    private Integer tokenVersion;       // Token版本号，密码重置后+1，用于使旧Token失效
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
