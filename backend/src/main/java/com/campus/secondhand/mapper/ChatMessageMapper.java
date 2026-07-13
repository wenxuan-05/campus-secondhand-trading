package com.campus.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.secondhand.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    @Update("UPDATE chat_messages SET is_read = 1 WHERE session_id = #{sessionId} AND sender_id = #{senderId} AND is_read = 0")
    int markRead(@Param("sessionId") String sessionId, @Param("senderId") Long senderId);
}
