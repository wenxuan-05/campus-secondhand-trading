package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.dto.CreateSessionDTO;
import com.campus.secondhand.dto.SessionVO;
import com.campus.secondhand.entity.ChatSession;

import java.util.List;

public interface ChatSessionService extends IService<ChatSession> {
    SessionVO createSession(CreateSessionDTO dto, Long initiatorId);
    List<SessionVO> getSessions(Long userId);
}
