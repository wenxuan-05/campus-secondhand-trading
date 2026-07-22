package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.CreateSessionDTO;
import com.campus.secondhand.dto.SessionVO;
import com.campus.secondhand.entity.ChatMessage;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.ChatMessageService;
import com.campus.secondhand.service.ChatSessionService;
import com.campus.secondhand.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatSessionService chatSessionService;
    private final FileService fileService;

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

    @PostMapping("/sessions")
    public Result<SessionVO> createSession(@Valid @RequestBody CreateSessionDTO dto) {
        return Result.ok(chatSessionService.createSession(dto, UserContext.getUserId()));
    }

    @GetMapping("/sessions")
    public Result<List<SessionVO>> sessions() {
        return Result.ok(chatSessionService.getSessions(UserContext.getUserId()));
    }

    /**
     * Upload image for chat messages.
     * Stores in MinIO under "chat/" prefix and returns the URL.
     */
    @PostMapping("/upload-image")
    public Result<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.upload(file, "chat");
        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("fileName", file.getOriginalFilename());
        return Result.ok(result);
    }
}
