package com.campustrade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campustrade.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM sys_user WHERE phone = #{phone}")
    User selectByPhone(String phone);

    @Select("SELECT * FROM sys_user WHERE user_no = #{userNo}")
    User selectByUserNo(String userNo);
}
