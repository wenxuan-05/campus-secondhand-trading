package com.campustrade.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentAuthRequest {

    @NotBlank(message = "学号不能为空")
    private String studentId;

    @NotBlank(message = "姓名不能为空")
    private String realName;

    @NotBlank(message = "身份证后4位不能为空")
    private String idCardSuffix;

    private String schoolId;
    private String campus;
    private String dormitory;
    private String roomNumber;
    private Integer gender;
}
