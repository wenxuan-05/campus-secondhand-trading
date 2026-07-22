package com.campus.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.secondhand.entity.UserBehaviorLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserBehaviorLogMapper extends BaseMapper<UserBehaviorLog> {

    /** 获取用户在各分类上的行为次数（加权），用于构建兴趣画像 */
    @Select("SELECT g.category, SUM(b.weight) AS total_weight, COUNT(*) AS cnt " +
            "FROM user_behavior_log b " +
            "JOIN goods g ON b.goods_id = g.id " +
            "WHERE b.user_id = #{userId} AND b.created_at >= #{since} " +
            "GROUP BY g.category ORDER BY total_weight DESC")
    List<Map<String, Object>> getUserCategoryPreferences(@Param("userId") Long userId,
                                                         @Param("since") String since);

    /** 获取用户近期交互过的所有商品ID（用于排除） */
    @Select("SELECT DISTINCT goods_id FROM user_behavior_log " +
            "WHERE user_id = #{userId} AND created_at >= #{since}")
    List<Long> getInteractedGoodsIds(@Param("userId") Long userId,
                                     @Param("since") String since);
}
