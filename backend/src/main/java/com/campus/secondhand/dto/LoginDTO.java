package com.campus.secondhand.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "学号或邮箱不能为空")
    private String studentId;
    @NotBlank(message = "密码不能为空")
    private String password;
}
