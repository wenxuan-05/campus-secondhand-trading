package com.campustrade.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {
    private Long id;
    private String userNo;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer status;
    private Integer creditScore;
    private String creditLevel;
    private Integer tradeCount;
    private Double goodRate;
    private Boolean isVerified;
    private String studentId;
    private String realName;
    private String schoolName;
    private String campus;
    private String dormitory;
}
