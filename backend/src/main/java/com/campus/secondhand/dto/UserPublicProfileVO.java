package com.campus.secondhand.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UserPublicProfileVO {
    private Long id;
    private String nickname;
    private String avatar;
    private String schoolName;
    private Integer creditScore;        // 信用分
    private String creditLevel;         // 信用等级：优秀/良好/一般/较差/极差
    private Integer tradeCount;         // 交易笔数
    private BigDecimal goodRate;        // 好评率
    private String dormitory;           // 宿舍楼
    private Integer gender;             // 0=未知, 1=男, 2=女
    private String realName;            // 真实姓名（已脱敏）
    private List<ReviewVO> reviews;     // 收到的评价（最近10条）
}
