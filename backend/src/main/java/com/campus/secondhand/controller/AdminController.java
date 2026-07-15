package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.Refund;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.GoodsService;
import com.campus.secondhand.service.OrderService;
import com.campus.secondhand.service.RefundService;
import com.campus.secondhand.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final GoodsService goodsService;
    private final OrderService orderService;
    private final RefundService refundService;

    // ==================== User Management ====================

    @GetMapping("/users")
    public Result<Page<User>> getUsers(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "20") int pageSize,
                                        @RequestParam(required = false) String keyword) {
        Page<User> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getStudentId, keyword)
                    .or().like(User::getNickname, keyword));
        }
        wrapper.orderByDesc(User::getCreatedAt);
        return Result.ok(userService.page(p, wrapper));
    }

    @PutMapping("/users/{id}/status")
    public Result<Void> toggleUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        User user = userService.getById(id);
        if (user != null) {
            user.setStatus(body.get("status"));
            userService.updateById(user);
        }
        return Result.ok();
    }

    /** Admin: adjust user credit score */
    @PutMapping("/users/{id}/credit")
    public Result<Void> adjustCredit(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        User user = userService.getById(id);
        if (user != null) {
            int delta = ((Number) body.getOrDefault("delta", 0)).intValue();
            user.setCreditScore(Math.max(0, user.getCreditScore() + delta));
            userService.updateById(user);
        }
        return Result.ok();
    }

    // ==================== Goods Management ====================

    @GetMapping("/goods")
    public Result<Page<Goods>> getGoods(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "20") int pageSize,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) Integer status) {
        Page<Goods> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Goods::getTitle, keyword);
        }
        if (status != null) {
            wrapper.eq(Goods::getStatus, status);
        }
        wrapper.orderByDesc(Goods::getCreatedAt);
        return Result.ok(goodsService.page(p, wrapper));
    }

    @DeleteMapping("/goods/{id}")
    public Result<Void> deleteGoods(@PathVariable Long id) {
        goodsService.removeById(id);
        return Result.ok();
    }

    @PutMapping("/goods/{id}/status")
    public Result<Void> toggleGoodsStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Goods goods = goodsService.getById(id);
        if (goods != null) {
            goods.setStatus(body.get("status"));
            goodsService.updateById(goods);
        }
        return Result.ok();
    }

    // ==================== Order Management ====================

    @GetMapping("/orders")
    public Result<Page<Order>> getOrders(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "20") int pageSize,
                                          @RequestParam(required = false) Integer status) {
        Page<Order> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>();
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreatedAt);
        return Result.ok(orderService.page(p, wrapper));
    }

    // ==================== Refund Management ====================

    @GetMapping("/refunds")
    public Result<Page<Refund>> getRefunds(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "20") int pageSize,
                                            @RequestParam(required = false) Integer status) {
        Page<Refund> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Refund> wrapper = new LambdaQueryWrapper<Refund>();
        if (status != null) {
            wrapper.eq(Refund::getStatus, status);
        }
        wrapper.orderByDesc(Refund::getCreatedAt);
        return Result.ok(refundService.page(p, wrapper));
    }

    @PostMapping("/refunds/{id}/arbitrate")
    public Result<Refund> arbitrateRefund(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        int decision = ((Number) body.getOrDefault("decision", 0)).intValue();
        String note = (String) body.getOrDefault("note", "");
        return Result.ok(refundService.arbitrate(id, decision, note));
    }

    // ==================== Dashboard Stats ====================

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        return Result.ok(Map.of(
                "totalUsers", userService.count(),
                "totalGoods", goodsService.count(),
                "totalOrders", orderService.count(),
                "onSaleGoods", goodsService.count(new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, 2)),
                "pendingRefunds", refundService.count(new LambdaQueryWrapper<Refund>().in(Refund::getStatus, 1, 4))
        ));
    }
}
