package com.campus.secondhand.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SendCodeDTO {
    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^\\d{8}$", message = "学号必须为8位数字")
    private String studentId;

    @NotBlank(message = "验证码类型不能为空")
    private String type;  // "register" 或 "reset"
}
