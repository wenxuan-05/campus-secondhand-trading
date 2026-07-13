package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.ChatMessage;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/history/{sessionId}")
    public Result<List<ChatMessage>> history(@PathVariable String sessionId,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "50") int pageSize) {
        return Result.ok(chatMessageService.getHistory(sessionId, page, pageSize));
    }

    @PutMapping("/read/{sessionId}/{senderId}")
    public Result<Void> markRead(@PathVariable String sessionId, @PathVariable Long senderId) {
        chatMessageService.markRead(sessionId, senderId);
        return Result.ok();
    }

    @GetMapping("/sessions")
    public Result<List<String>> sessions() {
        return Result.ok(chatMessageService.getSessionIds(UserContext.getUserId()));
    }
}
