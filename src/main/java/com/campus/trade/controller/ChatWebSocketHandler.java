package com.campus.trade.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> offlineMessages = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> userNames = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> readStatus = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        String query = session.getUri().getQuery();
        String name = "用户" + userId;
        if (query != null && query.contains("name=")) {
            String raw = query.split("name=")[1].split("&")[0];
            if (!raw.isEmpty()) {
                name = raw;
            }
        }
        userNames.put(userId, name);
        sessions.put(userId, session);
        System.out.println(name + "(" + userId + ") 已连接，当前在线人数：" + sessions.size());

        if (offlineMessages.containsKey(userId)) {
            session.sendMessage(new TextMessage(offlineMessages.get(userId)));
            offlineMessages.remove(userId);
            System.out.println("已推送离线消息给 " + name);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String fromUserId = getUserId(session);
        String fromName = userNames.getOrDefault(fromUserId, fromUserId);
        System.out.println("收到消息：" + payload);

        try {
            JsonNode json = objectMapper.readTree(payload);
            String type = json.has("type") ? json.get("type").asText() : "text";
            String content = json.has("content") ? json.get("content").asText() : "";
            String targetUserId = json.has("targetUserId") ? json.get("targetUserId").asText() : null;

            if ("typing".equals(type) && targetUserId != null) {
                String typingMsg = fromName + " 正在输入...";
                sendToUser(targetUserId, typingMsg, false);
                return;
            }

            if ("read".equals(type) && targetUserId != null) {
                String key = fromUserId + "_" + targetUserId;
                readStatus.put(key, true);
                String readMsg = fromName + " 已读消息";
                sendToUser(targetUserId, readMsg, false);
                return;
            }

            if ("bargain".equals(type)) {
                int price = json.has("price") ? json.get("price").asInt() : 0;
                String sellerMsg = "买家 " + fromName + " 出价 " + price + " 元，回复“同意”即可生成订单";
                sendToUser(targetUserId, sellerMsg, true);
                String buyerMsg = "您已向卖家 " + targetUserId + " 发起 " + price + " 元的议价，等待卖家回复";
                sendToUser(fromUserId, buyerMsg, false);
                String key = targetUserId + "_" + fromUserId;
                readStatus.put(key, false);
                return;
            }

            if ("agree".equals(type) && content.contains("同意")) {
                String agreeMsg = "🎉 卖家 " + fromName + " 已同意议价，订单已自动生成，请前往“我的订单”查看";
                sendToUser(targetUserId, agreeMsg, false);
                sendToUser(fromUserId, agreeMsg, false);
                return;
            }

            String broadcastMsg = fromName + " 说: " + content;
            for (WebSocketSession s : sessions.values()) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(broadcastMsg));
                }
            }

        } catch (Exception e) {
            String broadcastMsg = fromName + " 说: " + payload;
            for (WebSocketSession s : sessions.values()) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(broadcastMsg));
                }
            }
        }
    }

    private void sendToUser(String userId, String msg, boolean isOfflineStorable) throws Exception {
        WebSocketSession targetSession = sessions.get(userId);
        if (targetSession != null && targetSession.isOpen()) {
            targetSession.sendMessage(new TextMessage(msg));
        } else if (isOfflineStorable) {
            offlineMessages.put(userId, msg);
            System.out.println("用户 " + userId + " 离线，消息已暂存");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserId(session);
        String name = userNames.getOrDefault(userId, userId);
        sessions.remove(userId);
        userNames.remove(userId);
        System.out.println(name + "(" + userId + ") 已断开");
    }

    private String getUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("userId=")) {
            return query.split("userId=")[1].split("&")[0];
        }
        return session.getId();
    }
}