package com.campustrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("student_info")
public class StudentInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String studentId;
    private String realName;
    private String schoolId;
    private String schoolName;
    private String campus;
    private String dormitory;
    private String roomNumber;
    private Integer gender;
    private String idCardSuffix;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}