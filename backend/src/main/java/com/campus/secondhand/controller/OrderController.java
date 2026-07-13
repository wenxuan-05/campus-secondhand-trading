package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.OrderDTO;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Result<Order> create(@RequestBody OrderDTO dto) {
        return Result.ok(orderService.create(UserContext.getUserId(), dto.getGoodsId(), dto.getRemark()));
    }

    @PutMapping("/{id}/pay")
    public Result<Order> pay(@PathVariable Long id) {
        return Result.ok(orderService.pay(id, UserContext.getUserId()));
    }

    @PutMapping("/{id}/pickup-code")
    public Result<Map<String, String>> generatePickupCode(@PathVariable Long id) {
        String code = orderService.generatePickupCode(id, UserContext.getUserId());
        return Result.ok(Map.of("pickupCode", code));
    }

    @PostMapping("/{id}/verify-pickup")
    public Result<Order> verifyPickup(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return Result.ok(orderService.verifyPickup(id, UserContext.getUserId(), body.get("code")));
    }

    @PutMapping("/{id}/confirm")
    public Result<Order> confirm(@PathVariable Long id) {
        return Result.ok(orderService.confirmReceive(id, UserContext.getUserId()));
    }

    @PutMapping("/{id}/cancel")
    public Result<Order> cancel(@PathVariable Long id) {
        return Result.ok(orderService.cancel(id, UserContext.getUserId()));
    }

    @GetMapping("/buyer")
    public Result<Page<Order>> buyerOrders(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(orderService.buyerOrders(UserContext.getUserId(), page, pageSize));
    }

    @GetMapping("/seller")
    public Result<Page<Order>> sellerOrders(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(orderService.sellerOrders(UserContext.getUserId(), page, pageSize));
    }
}
