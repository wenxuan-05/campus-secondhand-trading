package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.dto.SessionVO;
import com.campus.secondhand.entity.ChatMessage;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.ChatMessageMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    private final UserMapper userMapper;

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
    public List<SessionVO> getSessions(Long userId) {
        // 1. Get all session IDs for this user
        QueryWrapper<ChatMessage> wrapper = new QueryWrapper<ChatMessage>()
                .and(w -> w.eq("sender_id", userId).or().eq("receiver_id", userId))
                .select("session_id")
                .groupBy("session_id")
                .orderByDesc("MAX(created_at)");
        List<String> sessionIds = list(wrapper).stream().map(ChatMessage::getSessionId).toList();

        // 2. For each session, collect metadata
        List<SessionVO> result = new ArrayList<>();
        for (String sid : sessionIds) {
            // Last message (use Page with size=1 for cross-database compatibility)
            Page<ChatMessage> lastPage = new Page<>(1, 1);
            LambdaQueryWrapper<ChatMessage> lastWrapper = new LambdaQueryWrapper<ChatMessage>()
                    .eq(ChatMessage::getSessionId, sid)
                    .orderByDesc(ChatMessage::getCreatedAt);
            lastPage = page(lastPage, lastWrapper);
            ChatMessage lastMsg = lastPage.getRecords().isEmpty() ? null : lastPage.getRecords().get(0);

            // Unread count: messages NOT from me AND not read
            long unread = count(new LambdaQueryWrapper<ChatMessage>()
                    .eq(ChatMessage::getSessionId, sid)
                    .ne(ChatMessage::getSenderId, userId)
                    .eq(ChatMessage::getIsRead, 0));

            // Parse the other user's ID from sessionId format: goods_gooodsId_uid1_uid2
            Long otherUserId = parseOtherUserId(sid, userId);
            User otherUser = otherUserId != null ? userMapper.selectById(otherUserId) : null;

            SessionVO vo = new SessionVO();
            vo.setSessionId(sid);
            vo.setOtherUserId(otherUserId);
            vo.setOtherUserName(otherUser != null ? otherUser.getNickname() : "未知用户");
            vo.setLastMessage(lastMsg != null ? lastMsg.getContent() : "");
            vo.setLastTime(lastMsg != null ? lastMsg.getCreatedAt() : null);
            vo.setUnreadCount((int) unread);
            result.add(vo);
        }
        return result;
    }

    private Long parseOtherUserId(String sessionId, Long myId) {
        String[] parts = sessionId.split("_");
        if (parts.length >= 4) {
            try {
                long uid1 = Long.parseLong(parts[2]);
                long uid2 = Long.parseLong(parts[3]);
                return uid1 == myId ? uid2 : uid1;
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }
}
