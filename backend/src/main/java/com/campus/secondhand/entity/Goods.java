package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("goods")
public class Goods {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer conditionLevel; // 1=全新, 2=99新, 3=95新, 4=9新, 5=战损
    private String category;
    private String images;          // JSON array string
    private String video;           // 视频URL
    private String isbn;            // ISBN编号（教材类）
    private String location;        // 交易地点JSON：{id, name, building}
    private String tags;            // 标签（JSON数组）
    private Integer collectCount;   // 收藏数
    private Integer chatCount;      // 聊天数
    private Integer negotiable;     // 可议价：0=否, 1=是
    private Integer shipping;       // 支持快递：0=否, 1=是
    private Long schoolId;          // 学校ID
    private Integer status;         // 0=草稿, 1=审核中, 2=在售, 3=已售, 4=已下架, 5=审核失败, 6=已删除
    private Integer viewCount;
    private LocalDateTime refreshTime;  // 刷新时间
    private LocalDateTime expireTime;   // 过期时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
