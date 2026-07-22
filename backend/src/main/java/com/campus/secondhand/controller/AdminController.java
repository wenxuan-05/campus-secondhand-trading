package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.AmbassadorApplication;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.Refund;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.security.RequireRole;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.AmbassadorApplicationService;
import com.campus.secondhand.service.GoodsService;
import com.campus.secondhand.service.OrderService;
import com.campus.secondhand.service.RefundService;
import com.campus.secondhand.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RequireRole({"ADMIN", "CAMPUS_AMBASSADOR"})
public class AdminController {

    private final UserService userService;
    private final GoodsService goodsService;
    private final OrderService orderService;
    private final RefundService refundService;
    private final AmbassadorApplicationService ambassadorApplicationService;

    // ==================== User Management (ADMIN only) ====================

    @GetMapping("/users")
    @RequireRole("ADMIN")
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
        Page<User> result = userService.page(p, wrapper);
        // 补全空角色、脱敏密码
        result.getRecords().forEach(u -> {
            if (u.getRole() == null || u.getRole().isEmpty()) u.setRole("USER");
            u.setPassword(null);
        });
        return Result.ok(result);
    }

    @PutMapping("/users/{id}/status")
    @RequireRole("ADMIN")
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
    @RequireRole("ADMIN")
    public Result<Void> adjustCredit(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        User user = userService.getById(id);
        if (user != null) {
            int delta = ((Number) body.getOrDefault("delta", 0)).intValue();
            user.setCreditScore(Math.max(0, user.getCreditScore() + delta));
            userService.updateById(user);
        }
        return Result.ok();
    }

    /** Set user as campus ambassador with worker ID */
    @PutMapping("/users/{id}/ambassador")
    @RequireRole("ADMIN")
    public Result<Void> setAmbassador(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userService.getById(id);
        if (user == null) return Result.fail(404, "用户不存在");
        if ("ADMIN".equals(user.getRole())) return Result.fail(400, "不能将管理员设为校园大使");
        String workerId = body.get("workerId");
        if (!StringUtils.hasText(workerId)) return Result.fail(400, "工号不能为空");
        // 检查工号唯一性
        long dup = userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getWorkerId, workerId));
        if (dup > 0) return Result.fail(400, "该工号已被使用");
        user.setRole("CAMPUS_AMBASSADOR");
        user.setWorkerId(workerId);
        user.setIsCampusAmbassador(1);
        userService.updateById(user);
        return Result.ok();
    }

    /** Demote campus ambassador to regular user */
    @PutMapping("/users/{id}/demote")
    @RequireRole("ADMIN")
    public Result<Void> demoteAmbassador(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) return Result.fail(404, "用户不存在");
        if (!"CAMPUS_AMBASSADOR".equals(user.getRole())) {
            return Result.fail(400, "该用户不是校园大使");
        }
        user.setRole("USER");
        user.setWorkerId(null);
        user.setIsCampusAmbassador(0);
        userService.updateById(user);
        return Result.ok();
    }

    /** Delete user */
    @DeleteMapping("/users/{id}")
    @RequireRole("ADMIN")
    public Result<Void> deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) return Result.fail(404, "用户不存在");
        // 禁止删除自己
        Long currentUserId = UserContext.getUserId();
        if (currentUserId != null && currentUserId.equals(id)) {
            return Result.fail(400, "不能删除自己");
        }
        userService.removeById(id);
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
    @RequireRole("ADMIN")
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

    // ==================== Ambassador Application Review (ADMIN only) ====================

    /** 校园大使申请列表 */
    @GetMapping("/ambassador-applications")
    @RequireRole("ADMIN")
    public Result<Page<AmbassadorApplication>> getAmbassadorApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Integer status) {
        Page<AmbassadorApplication> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<AmbassadorApplication> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(AmbassadorApplication::getStatus, status);
        }
        wrapper.orderByAsc(AmbassadorApplication::getStatus)    // 待审核优先
               .orderByDesc(AmbassadorApplication::getCreatedAt);
        return Result.ok(ambassadorApplicationService.page(p, wrapper));
    }

    /** 审核通过：批准校园大使申请 */
    @PutMapping("/ambassador-applications/{id}/approve")
    @RequireRole("ADMIN")
    public Result<Void> approveAmbassadorApplication(@PathVariable Long id, @RequestBody Map<String, String> body) {
        AmbassadorApplication app = ambassadorApplicationService.getById(id);
        if (app == null) return Result.fail(404, "申请不存在");
        if (app.getStatus() != 0) return Result.fail(400, "该申请已处理");

        app.setStatus(1);
        app.setReviewerId(UserContext.getUserId());
        app.setReviewedAt(java.time.LocalDateTime.now());
        String note = body.getOrDefault("note", "");
        if (StringUtils.hasText(note)) {
            app.setReviewNote(note);
        }
        ambassadorApplicationService.updateById(app);

        // 同步更新用户角色为校园大使
        if (app.getUserId() != null) {
            User user = userService.getById(app.getUserId());
            if (user != null && !"ADMIN".equals(user.getRole())) {
                user.setRole("CAMPUS_AMBASSADOR");
                user.setIsCampusAmbassador(1);
                userService.updateById(user);
            }
        }

        return Result.ok();
    }

    /** 审核驳回 */
    @PutMapping("/ambassador-applications/{id}/reject")
    @RequireRole("ADMIN")
    public Result<Void> rejectAmbassadorApplication(@PathVariable Long id, @RequestBody Map<String, String> body) {
        AmbassadorApplication app = ambassadorApplicationService.getById(id);
        if (app == null) return Result.fail(404, "申请不存在");
        if (app.getStatus() != 0) return Result.fail(400, "该申请已处理");

        app.setStatus(2);
        app.setReviewerId(UserContext.getUserId());
        app.setReviewedAt(java.time.LocalDateTime.now());
        String note = body.getOrDefault("note", "");
        if (!StringUtils.hasText(note)) {
            note = "感谢你的申请，暂不符合校园大使要求，请继续关注后续招募。";
        }
        app.setReviewNote(note);
        ambassadorApplicationService.updateById(app);
        return Result.ok();
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
