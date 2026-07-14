package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.GoodsService;
import com.campus.secondhand.service.OrderService;
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

    /**
     * Check if current user is admin, throw exception if not.
     */
    private void requireAdmin() {
        if (!UserContext.isAdmin()) {
            throw new BusinessException("无管理员权限");
        }
    }

    // ==================== User Management ====================

    @GetMapping("/users")
    public Result<Page<User>> getUsers(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "20") int pageSize,
                                        @RequestParam(required = false) String keyword) {
        requireAdmin();
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
        requireAdmin();
        User user = userService.getById(id);
        if (user != null) {
            user.setStatus(body.get("status"));
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
        requireAdmin();
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
        requireAdmin();
        goodsService.removeById(id);
        return Result.ok();
    }

    @PutMapping("/goods/{id}/status")
    public Result<Void> toggleGoodsStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        requireAdmin();
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
        requireAdmin();
        Page<Order> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>();
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreatedAt);
        return Result.ok(orderService.page(p, wrapper));
    }

    // ==================== Dashboard Stats ====================

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        requireAdmin();
        return Result.ok(Map.of(
                "totalUsers", userService.count(),
                "totalGoods", goodsService.count(),
                "totalOrders", orderService.count(),
                "onSaleGoods", goodsService.count(new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, 1))
        ));
    }
}
