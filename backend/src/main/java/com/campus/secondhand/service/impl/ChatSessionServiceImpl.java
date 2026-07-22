package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.CreateSessionDTO;
import com.campus.secondhand.dto.SessionVO;
import com.campus.secondhand.entity.BuyRequest;
import com.campus.secondhand.entity.ChatMessage;
import com.campus.secondhand.entity.ChatSession;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.BuyRequestMapper;
import com.campus.secondhand.mapper.ChatMessageMapper;
import com.campus.secondhand.mapper.ChatSessionMapper;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.service.ChatSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements ChatSessionService {

    private final BuyRequestMapper buyRequestMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final UserMapper userMapper;
    private final GoodsMapper goodsMapper;

    @Override
    public SessionVO createSession(CreateSessionDTO dto, Long initiatorId) {
        BuyRequest br = buyRequestMapper.selectById(dto.getBuyRequestId());
        if (br == null) throw new BusinessException("求购信息不存在");

        // Build session ID: buy_{requestId}_{sorted_uid1}_{sorted_uid2}
        long smaller = Math.min(initiatorId, dto.getTargetId());
        long larger = Math.max(initiatorId, dto.getTargetId());
        String sessionId = "buy_" + dto.getBuyRequestId() + "_" + smaller + "_" + larger;

        // Check if session already exists
        ChatSession existing = getOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getSessionId, sessionId));
        if (existing != null) {
            return buildSessionVO(existing, initiatorId);
        }

        // Update buy request status to "沟通中"
        if (br.getStatus() == 1) {
            br.setStatus(2);
            buyRequestMapper.updateById(br);
        }

        ChatSession cs = new ChatSession();
        cs.setSessionId(sessionId);
        cs.setBuyRequestId(dto.getBuyRequestId());
        cs.setInitiatorId(initiatorId);
        cs.setTargetId(dto.getTargetId());
        cs.setLastMessage("");
        cs.setLastTime(LocalDateTime.now());
        save(cs);

        return buildSessionVO(cs, initiatorId);
    }

    @Override
    public List<SessionVO> getSessions(Long userId) {
        // Get all chat sessions involving this user (both goods_ and buy_ prefixed)
        // First, get chat_sessions table entries
        List<ChatSession> chatSessions = list(new LambdaQueryWrapper<ChatSession>()
                .and(w -> w.eq(ChatSession::getInitiatorId, userId).or().eq(ChatSession::getTargetId, userId))
                .orderByDesc(ChatSession::getLastTime));

        // Also keep backward compatibility: derive sessions from chat_messages
        // (for goods_ prefixed sessions that predate the chat_sessions table)
        java.util.Set<String> seenSessionIds = new java.util.HashSet<>();
        List<SessionVO> result = new ArrayList<>();

        for (ChatSession cs : chatSessions) {
            seenSessionIds.add(cs.getSessionId());
            result.add(buildSessionVO(cs, userId));
        }

        // Fallback: add legacy goods_ sessions from chat_messages
        List<ChatMessage> distinctMessages = chatMessageMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ChatMessage>()
                        .select("session_id, MAX(created_at) as created_at")
                        .and(w -> w.eq("sender_id", userId).or().eq("receiver_id", userId))
                        .groupBy("session_id")
                        .orderByDesc("MAX(created_at)")
        );
        for (ChatMessage cm : distinctMessages) {
            String sid = cm.getSessionId();
            if (!seenSessionIds.contains(sid) && sid.startsWith("goods_")) {
                SessionVO vo = buildLegacySessionVO(sid, userId);
                if (vo != null) result.add(vo);
            }
        }

        return result;
    }

    private SessionVO buildSessionVO(ChatSession cs, Long currentUserId) {
        Long otherUserId = cs.getInitiatorId().equals(currentUserId) ? cs.getTargetId() : cs.getInitiatorId();
        User otherUser = userMapper.selectById(otherUserId);

        SessionVO vo = new SessionVO();
        vo.setSessionId(cs.getSessionId());
        vo.setOtherUserId(otherUserId);
        vo.setOtherUserName(otherUser != null ? otherUser.getNickname() : "未知用户");
        vo.setOtherUserAvatar(otherUser != null ? otherUser.getAvatar() : null);
        vo.setLastMessage(cs.getLastMessage() != null ? cs.getLastMessage() : "");
        vo.setLastTime(cs.getLastTime());

        // Count unread messages
        long unread = chatMessageMapper.selectCount(new LambdaQueryWrapper<ChatMessage>()
                .eq(ChatMessage::getSessionId, cs.getSessionId())
                .ne(ChatMessage::getSenderId, currentUserId)
                .eq(ChatMessage::getIsRead, 0));
        vo.setUnreadCount((int) unread);

        // Parse goods info from sessionId
        populateGoodsInfo(vo, cs.getSessionId());

        return vo;
    }

    private SessionVO buildLegacySessionVO(String sessionId, Long currentUserId) {
        // Parse other user from sessionId format: goods_{goodsId}_{uid1}_{uid2}
        String[] parts = sessionId.split("_");
        if (parts.length < 4) return null;
        try {
            long uid1 = Long.parseLong(parts[2]);
            long uid2 = Long.parseLong(parts[3]);
            long otherUserId = (uid1 == currentUserId) ? uid2 : uid1;
            User otherUser = userMapper.selectById(otherUserId);

            // Get last message
            var lastPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<ChatMessage>(1, 1);
            var lastWrapper = new LambdaQueryWrapper<ChatMessage>()
                    .eq(ChatMessage::getSessionId, sessionId)
                    .orderByDesc(ChatMessage::getCreatedAt);
            var lastResult = chatMessageMapper.selectPage(lastPage, lastWrapper);
            ChatMessage lastMsg = lastResult.getRecords().isEmpty() ? null : lastResult.getRecords().get(0);

            long unread = chatMessageMapper.selectCount(new LambdaQueryWrapper<ChatMessage>()
                    .eq(ChatMessage::getSessionId, sessionId)
                    .ne(ChatMessage::getSenderId, currentUserId)
                    .eq(ChatMessage::getIsRead, 0));

            SessionVO vo = new SessionVO();
            vo.setSessionId(sessionId);
            vo.setOtherUserId(otherUserId);
            vo.setOtherUserName(otherUser != null ? otherUser.getNickname() : "未知用户");
            vo.setOtherUserAvatar(otherUser != null ? otherUser.getAvatar() : null);
            vo.setLastMessage(lastMsg != null ? lastMsg.getContent() : "");
            vo.setLastTime(lastMsg != null ? lastMsg.getCreatedAt() : null);
            vo.setUnreadCount((int) unread);

            // Parse goods info from sessionId
            populateGoodsInfo(vo, sessionId);

            return vo;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parse goodsId from sessionId and populate goods info.
     * SessionId format: goods_{goodsId}_{uid1}_{uid2}
     */
    private void populateGoodsInfo(SessionVO vo, String sessionId) {
        if (sessionId == null || !sessionId.startsWith("goods_")) return;
        String[] parts = sessionId.split("_");
        if (parts.length >= 2) {
            try {
                Long goodsId = Long.parseLong(parts[1]);
                vo.setGoodsId(goodsId);
                Goods goods = goodsMapper.selectById(goodsId);
                if (goods != null) {
                    vo.setGoodsTitle(goods.getTitle());
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
