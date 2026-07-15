package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.entity.ChatMessage;
import com.campus.secondhand.mapper.ChatMessageMapper;
import com.campus.secondhand.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    @Override
    public List<ChatMessage> getHistory(String sessionId, int page, int pageSize) {
        Page<ChatMessage> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<ChatMessage>()
                .eq(ChatMessage::getSessionId, sessionId)
                .orderByDesc(ChatMessage::getCreatedAt);
        return page(p, wrapper).getRecords();
    }

    @Override
    public void markRead(String sessionId, Long senderId) {
        baseMapper.markRead(sessionId, senderId);
    }

    @Override
    public List<String> getSessionIds(Long userId) {
        // Use QueryWrapper (not LambdaQueryWrapper) to allow raw SQL aggregate in ORDER BY
        QueryWrapper<ChatMessage> wrapper = new QueryWrapper<ChatMessage>()
                .and(w -> w.eq("sender_id", userId).or().eq("receiver_id", userId))
                .select("session_id")
                .groupBy("session_id")
                .orderByDesc("MAX(created_at)");
        return list(wrapper).stream().map(ChatMessage::getSessionId).toList();
    }
}
