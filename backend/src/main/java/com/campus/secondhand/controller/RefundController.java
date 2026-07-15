package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.RefundDTO;
import com.campus.secondhand.entity.Refund;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.RefundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public Result<Refund> apply(@Valid @RequestBody RefundDTO dto) {
        String imagesJson = "[]";
        try {
            imagesJson = dto.getImages() != null ? objectMapper.writeValueAsString(dto.getImages()) : "[]";
        } catch (JsonProcessingException ignored) {}
        return Result.ok(refundService.applyRefund(UserContext.getUserId(), dto.getOrderId(), dto.getReason(), imagesJson));
    }

    @PostMapping("/{id}/approve")
    public Result<Refund> approve(@PathVariable Long id) {
        return Result.ok(refundService.approveRefund(UserContext.getUserId(), id));
    }

    @PostMapping("/{id}/reject")
    public Result<Refund> reject(@PathVariable Long id) {
        return Result.ok(refundService.rejectRefund(UserContext.getUserId(), id));
    }

    /** Admin: arbitrate refund dispute */
    @PostMapping("/{id}/arbitrate")
    public Result<Refund> arbitrate(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        int decision = (int) body.getOrDefault("decision", 0);
        String note = (String) body.getOrDefault("note", "");
        return Result.ok(refundService.arbitrate(id, decision, note));
    }
}
