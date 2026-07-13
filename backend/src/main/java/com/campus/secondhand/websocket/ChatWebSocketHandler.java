package com.campus.secondhand.websocket;

import com.campus.secondhand.entity.ChatMessage;
import com.campus.secondhand.service.ChatMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatMessageService chatMessageService;
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

        // Persist message
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(sessionId);
        chatMessage.setSenderId(senderId);
        chatMessage.setReceiverId(receiverId);
        chatMessage.setContent(content);
        chatMessage.setType(type);
        chatMessage.setIsRead(0);
        chatMessageService.save(chatMessage);

        // Deliver to receiver in the same room
        ConcurrentHashMap<Long, WebSocketSession> room = ROOMS.get(sessionId);
        if (room == null) {
            room = new ConcurrentHashMap<>();
            ROOMS.put(sessionId, room);
        }
        // Register sender
        room.put(senderId, session);

        String json = objectMapper.writeValueAsString(Map.of(
                "id", chatMessage.getId(),
                "sessionId", sessionId,
                "senderId", senderId,
                "receiverId", receiverId,
                "content", content,
                "type", type,
                "createdAt", chatMessage.getCreatedAt().toString()
        ));

        // Send to receiver if online
        WebSocketSession receiverSession = room.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(json));
        }

        // Echo back to sender
        session.sendMessage(new TextMessage(json));
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
}
