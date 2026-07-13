package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService extends IService<ChatMessage> {
    List<ChatMessage> getHistory(String sessionId, int page, int pageSize);
    void markRead(String sessionId, Long senderId);
    List<String> getSessionIds(Long userId);
}
