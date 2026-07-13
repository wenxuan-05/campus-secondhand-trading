package com.campustrade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campustrade.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    @Select("SELECT * FROM user_auth WHERE user_id = #{userId} AND auth_type = #{authType}")
    UserAuth selectByUserAndType(Long userId, Integer authType);
}
