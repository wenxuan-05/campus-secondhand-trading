package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ambassador_applications")
public class AmbassadorApplication {
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 申请人用户ID（已登录用户）或 null（未登录填写） */
    private Long userId;

    /** 姓名 */
    private String name;

    /** 学号 */
    private String studentId;

    /** 手机号 */
    private String phone;

    /** 学院/专业 */
    private String department;

    /** 宿舍楼栋 */
    private String dormitory;

    /** 社群资源类型：dormitory/department/club/other */
    private String communityResource;

    /** 申请理由（≤200字） */
    private String reason;

    /** 审核状态：0=待审核, 1=通过, 2=驳回 */
    private Integer status;

    /** 审核人ID（管理员） */
    private Long reviewerId;

    /** 审核备注 */
    private String reviewNote;

    /** 审核时间 */
    private LocalDateTime reviewedAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
