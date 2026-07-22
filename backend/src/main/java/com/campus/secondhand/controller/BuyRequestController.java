package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.BuyRequestDTO;
import com.campus.secondhand.dto.BuyRequestQueryDTO;
import com.campus.secondhand.entity.BuyRequest;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.BuyRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buy-requests")
@RequiredArgsConstructor
public class BuyRequestController {

    private final BuyRequestService buyRequestService;

    @PostMapping
    public Result<BuyRequest> publish(@Valid @RequestBody BuyRequestDTO dto) {
        return Result.ok(buyRequestService.publish(dto, UserContext.getUserId()));
    }

    @DeleteMapping("/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        buyRequestService.cancel(id, UserContext.getUserId());
        return Result.ok();
    }

    @GetMapping("/my")
    public Result<Page<BuyRequest>> myRequests(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(buyRequestService.myRequests(UserContext.getUserId(), page, pageSize));
    }

    @GetMapping("/search")
    public Result<Page<BuyRequest>> search(BuyRequestQueryDTO query) {
        return Result.ok(buyRequestService.search(query));
    }

    @GetMapping("/{id}")
    public Result<BuyRequest> detail(@PathVariable Long id) {
        return Result.ok(buyRequestService.getDetail(id));
    }
}
