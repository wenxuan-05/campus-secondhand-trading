package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.AmbassadorApplication;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.security.RequireRole;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.AmbassadorApplicationService;
import com.campus.secondhand.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ambassador")
@RequiredArgsConstructor
public class AmbassadorController {

    private final GoodsService goodsService;
    private final AmbassadorApplicationService ambassadorApplicationService;

    // ==================== 校园大使申请（公开接口） ====================

    /** 提交校园大使申请 */
    @PostMapping("/apply")
    public Result<AmbassadorApplication> apply(@RequestBody AmbassadorApplication application) {
        if (!StringUtils.hasText(application.getName()) || application.getName().length() < 2) {
            return Result.fail("姓名不能为空且至少2个字符");
        }
        if (!StringUtils.hasText(application.getStudentId())) {
            return Result.fail("学号不能为空");
        }
        if (!StringUtils.hasText(application.getPhone())) {
            return Result.fail("手机号不能为空");
        }
        if (!StringUtils.hasText(application.getDepartment())) {
            return Result.fail("学院/专业不能为空");
        }
        if (!StringUtils.hasText(application.getDormitory())) {
            return Result.fail("请选择宿舍楼栋");
        }
        if (!StringUtils.hasText(application.getCommunityResource())) {
            return Result.fail("请选择社群资源类型");
        }
        if (!StringUtils.hasText(application.getReason()) || application.getReason().length() < 10) {
            return Result.fail("申请理由至少10个字");
        }
        if (application.getReason().length() > 200) {
            return Result.fail("申请理由不超过200字");
        }

        // 检查是否已经提交过待审核的申请
        long existing = ambassadorApplicationService.count(new LambdaQueryWrapper<AmbassadorApplication>()
                .eq(AmbassadorApplication::getStudentId, application.getStudentId())
                .eq(AmbassadorApplication::getStatus, 0));
        if (existing > 0) {
            return Result.fail("你已提交过申请，请耐心等待审核");
        }

        // 如果已登录，关联用户ID
        try {
            Long currentUserId = UserContext.getUserId();
            if (currentUserId != null) {
                application.setUserId(currentUserId);
            }
        } catch (Exception ignored) {
            // 未登录也可以提交
        }

        application.setStatus(0);
        ambassadorApplicationService.save(application);
        return Result.ok(application);
    }

    /** 查询自己的申请状态 */
    @GetMapping("/application/status")
    public Result<AmbassadorApplication> getApplicationStatus(@RequestParam String studentId) {
        AmbassadorApplication app = ambassadorApplicationService.getOne(
                new LambdaQueryWrapper<AmbassadorApplication>()
                        .eq(AmbassadorApplication::getStudentId, studentId)
                        .orderByDesc(AmbassadorApplication::getCreatedAt)
                        .last("LIMIT 1"));
        return Result.ok(app);
    }

    // ==================== 商品审核 ====================

    /** 待审核商品列表（status=1 审核中） */
    @GetMapping("/goods/pending")
    @RequireRole({"ADMIN", "CAMPUS_AMBASSADOR"})
    public Result<Page<Goods>> getPendingGoods(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword) {
        Page<Goods> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>()
                .eq(Goods::getStatus, 1);
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Goods::getTitle, keyword);
        }
        wrapper.orderByAsc(Goods::getCreatedAt); // 最早提交的优先审核
        return Result.ok(goodsService.page(p, wrapper));
    }

    /** 审核通过（status 1→2 在售） */
    @PutMapping("/goods/{id}/approve")
    @RequireRole({"ADMIN", "CAMPUS_AMBASSADOR"})
    public Result<Goods> approveGoods(@PathVariable Long id) {
        Goods goods = goodsService.getById(id);
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (goods.getStatus() != 1) throw new BusinessException("只能审核待审核状态的商品");
        goods.setStatus(2);
        goods.setReviewerId(UserContext.getUserId());
        goods.setReviewedAt(LocalDateTime.now());
        goods.setReviewReason(null);
        goods.setExpireTime(LocalDateTime.now().plusDays(30));
        goodsService.updateById(goods);
        return Result.ok(goodsService.getById(id));
    }

    /** 审核驳回（status 1→5 审核失败） */
    @PutMapping("/goods/{id}/reject")
    @RequireRole({"ADMIN", "CAMPUS_AMBASSADOR"})
    public Result<Goods> rejectGoods(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Goods goods = goodsService.getById(id);
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (goods.getStatus() != 1) throw new BusinessException("只能审核待审核状态的商品");
        String reason = body.getOrDefault("reason", "不符合平台规范");
        if (!StringUtils.hasText(reason)) reason = "不符合平台规范";
        goods.setStatus(5);
        goods.setReviewReason(reason);
        goods.setReviewerId(UserContext.getUserId());
        goods.setReviewedAt(LocalDateTime.now());
        goodsService.updateById(goods);
        return Result.ok(goodsService.getById(id));
    }

    /** 我的审核记录 */
    @GetMapping("/goods/reviewed")
    @RequireRole({"ADMIN", "CAMPUS_AMBASSADOR"})
    public Result<Page<Goods>> getReviewedGoods(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<Goods> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>()
                .eq(Goods::getReviewerId, UserContext.getUserId())
                .in(Goods::getStatus, 2, 5)
                .orderByDesc(Goods::getReviewedAt);
        return Result.ok(goodsService.page(p, wrapper));
    }

    // ==================== 推广数据统计 ====================

    /** 校园大使推广数据看板 */
    @GetMapping("/stats")
    @RequireRole({"ADMIN", "CAMPUS_AMBASSADOR"})
    public Result<Map<String, Object>> getStats() {
        Long reviewerId = UserContext.getUserId();
        LambdaQueryWrapper<Goods> reviewed = new LambdaQueryWrapper<Goods>()
                .eq(Goods::getReviewerId, reviewerId);

        long totalReviewed = goodsService.count(reviewed);
        long approved = goodsService.count(reviewed.clone().eq(Goods::getStatus, 2));
        long rejected = goodsService.count(reviewed.clone().eq(Goods::getStatus, 5));
        long pending = goodsService.count(new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, 1));

        // 今日审核量
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long todayReviewed = goodsService.count(reviewed.clone()
                .ge(Goods::getReviewedAt, todayStart));
        long todayApproved = goodsService.count(reviewed.clone()
                .eq(Goods::getStatus, 2).ge(Goods::getReviewedAt, todayStart));
        long todayRejected = goodsService.count(reviewed.clone()
                .eq(Goods::getStatus, 5).ge(Goods::getReviewedAt, todayStart));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviewed", totalReviewed);
        stats.put("approved", approved);
        stats.put("rejected", rejected);
        stats.put("pendingTotal", pending);
        stats.put("approvalRate", totalReviewed > 0
                ? Math.round(approved * 10000.0 / totalReviewed) / 100.0 : 0);
        stats.put("todayReviewed", todayReviewed);
        stats.put("todayApproved", todayApproved);
        stats.put("todayRejected", todayRejected);
        return Result.ok(stats);
    }
}
