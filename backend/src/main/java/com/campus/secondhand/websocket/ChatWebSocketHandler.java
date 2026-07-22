package com.campus.secondhand.websocket;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.secondhand.entity.ChatMessage;
import com.campus.secondhand.entity.ChatSession;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.mapper.ChatSessionMapper;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.service.ChatMessageService;
import com.campus.secondhand.service.SensitiveWordFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatMessageService chatMessageService;
    private final ChatSessionMapper chatSessionMapper;
    private final GoodsMapper goodsMapper;
    private final SensitiveWordFilter sensitiveWordFilter;
    private final ObjectMapper objectMapper;

    // sessionId -> (userId -> WebSocketSession)
    private static final ConcurrentHashMap<String, ConcurrentHashMap<Long, WebSocketSession>> ROOMS = new ConcurrentHashMap<>();
    // WebSocketSession.id -> userId
    private static final ConcurrentHashMap<String, Long> SESSION_USERS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            SESSION_USERS.put(session.getId(), userId);
            log.info("WebSocket connected: userId={}, sessionId={}", userId, session.getId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long senderId = SESSION_USERS.get(session.getId());
        if (senderId == null) return;

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
        String sessionId = (String) payload.get("sessionId");
        Long receiverId = payload.get("receiverId") instanceof Integer
                ? ((Integer) payload.get("receiverId")).longValue()
                : (Long) payload.get("receiverId");
        String content = (String) payload.get("content");
        Integer type = payload.get("type") instanceof Integer
                ? (Integer) payload.get("type") : 1;
        String extraData = (String) payload.get("extraData");

        // --- Type-specific processing ---
        boolean wasFiltered = false;
        String originalContent = content;

        switch (type) {
            case 1: // Text — filter sensitive words
                if (content != null) {
                    content = sensitiveWordFilter.filter(content);
                    if (!content.equals(originalContent)) {
                        wasFiltered = true;
                    }
                }
                break;

            case 2: // Image — content is already the URL, extraData may have metadata
                if (content == null || content.isBlank()) {
                    sendError(session, "图片消息不能为空");
                    return;
                }
                break;

            case 3: // Product card — goodsId is in payload
                Object goodsIdObj = payload.get("goodsId");
                if (goodsIdObj != null) {
                    Long goodsId = goodsIdObj instanceof Integer
                            ? ((Integer) goodsIdObj).longValue()
                            : (Long) goodsIdObj;
                    Goods goods = goodsMapper.selectById(goodsId);
                    if (goods != null) {
                        Map<String, Object> cardData = new HashMap<>();
                        cardData.put("goodsId", goods.getId());
                        cardData.put("title", goods.getTitle());
                        cardData.put("price", goods.getPrice());
                        cardData.put("image", extractFirstImage(goods.getImages()));
                        extraData = objectMapper.writeValueAsString(cardData);
                        if (content == null || content.isBlank()) {
                            content = "[商品分享] " + goods.getTitle();
                        }
                    }
                }
                break;

            case 4: // Bid — extraData contains bid amount
                if (extraData == null) {
                    // Build bid extraData from payload
                    Object bidAmount = payload.get("bidAmount");
                    if (bidAmount != null) {
                        Map<String, Object> bidData = new HashMap<>();
                        bidData.put("amount", bidAmount);
                        bidData.put("status", "pending");
                        extraData = objectMapper.writeValueAsString(bidData);
                    }
                    if (content == null || content.isBlank()) {
                        content = "[出价] ¥" + (bidAmount != null ? bidAmount : "");
                    }
                }
                break;

            default:
                break;
        }

        // --- Persist message ---
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(sessionId);
        chatMessage.setSenderId(senderId);
        chatMessage.setReceiverId(receiverId);
        chatMessage.setContent(content);
        chatMessage.setType(type);
        chatMessage.setExtraData(extraData);
        chatMessage.setIsRead(0);
        chatMessageService.save(chatMessage);

        // --- Sync chat_sessions table ---
        ChatSession cs = chatSessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getSessionId, sessionId));
        if (cs != null) {
            cs.setLastMessage(content);
            cs.setLastTime(LocalDateTime.now());
            chatSessionMapper.updateById(cs);
        } else {
            // Auto-create session for goods_ prefixed legacy sessions
            if (sessionId != null && sessionId.startsWith("goods_")) {
                ChatSession newCs = new ChatSession();
                newCs.setSessionId(sessionId);
                newCs.setInitiatorId(senderId);
                newCs.setTargetId(receiverId);
                newCs.setLastMessage(content);
                newCs.setLastTime(LocalDateTime.now());
                chatSessionMapper.insert(newCs);

                // Increment goods.chatCount for first contact
                Long goodsId = parseGoodsId(sessionId);
                if (goodsId != null) {
                    Goods goods = goodsMapper.selectById(goodsId);
                    if (goods != null) {
                        Integer currentCount = goods.getChatCount();
                        goods.setChatCount(currentCount != null ? currentCount + 1 : 1);
                        goodsMapper.updateById(goods);
                    }
                }
            }
        }

        // --- Register sender in room ---
        ConcurrentHashMap<Long, WebSocketSession> room = ROOMS.get(sessionId);
        if (room == null) {
            room = new ConcurrentHashMap<>();
            ROOMS.put(sessionId, room);
        }
        room.put(senderId, session);

        // --- Build response JSON ---
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", chatMessage.getId());
        responseData.put("sessionId", sessionId);
        responseData.put("senderId", senderId);
        responseData.put("receiverId", receiverId);
        responseData.put("content", content);
        responseData.put("type", type);
        responseData.put("extraData", extraData);
        responseData.put("isRead", 0);
        responseData.put("createdAt", chatMessage.getCreatedAt().toString());

        String json = objectMapper.writeValueAsString(responseData);

        // --- Deliver to receiver if online ---
        WebSocketSession receiverSession = room.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(json));
        }

        // --- Echo back to sender ---
        // If message was filtered, also send a notification
        session.sendMessage(new TextMessage(json));

        if (wasFiltered) {
            Map<String, Object> blockedNotice = new HashMap<>();
            blockedNotice.put("type", "system");
            blockedNotice.put("event", "message_filtered");
            blockedNotice.put("message", "您的消息包含敏感词，已被过滤处理");
            blockedNotice.put("sessionId", sessionId);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(blockedNotice)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = SESSION_USERS.remove(session.getId());
        if (userId != null) {
            ROOMS.values().forEach(room -> room.remove(userId));
            log.info("WebSocket disconnected: userId={}", userId);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket error: sessionId={}", session.getId(), exception);
    }

    private void sendError(WebSocketSession session, String msg) {
        try {
            Map<String, Object> error = new HashMap<>();
            error.put("type", "system");
            error.put("event", "error");
            error.put("message", msg);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(error)));
        } catch (IOException ignored) {
        }
    }

    /**
     * Extract the first image URL from a JSON array string.
     */
    private String extractFirstImage(String imagesJson) {
        if (imagesJson == null || imagesJson.isBlank() || "[]".equals(imagesJson.trim())) {
            return null;
        }
        try {
            String[] arr = objectMapper.readValue(imagesJson, String[].class);
            return arr.length > 0 ? arr[0] : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parse goodsId from sessionId format: goods_{goodsId}_{uid1}_{uid2}
     */
    private Long parseGoodsId(String sessionId) {
        if (sessionId == null || !sessionId.startsWith("goods_")) return null;
        String[] parts = sessionId.split("_");
        if (parts.length >= 2) {
            try {
                return Long.parseLong(parts[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }
}
