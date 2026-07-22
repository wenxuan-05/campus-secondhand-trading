package com.campus.secondhand.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSessionDTO {
    @NotNull(message = "求购ID不能为空")
    private Long buyRequestId;

    @NotNull(message = "目标用户不能为空")
    private Long targetId;
}
