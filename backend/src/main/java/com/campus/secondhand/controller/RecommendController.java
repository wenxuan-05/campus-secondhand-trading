package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.RecommendResultDTO;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    /**
     * 获取个性化推荐商品
     * @param limit 推荐数量（默认15）
     * @param seed  随机种子，非0时打乱候选集（用于"换一批"）
     */
    @GetMapping
    public Result<List<RecommendResultDTO>> recommend(@RequestParam(defaultValue = "15") int limit,
                                                      @RequestParam(defaultValue = "0") long seed) {
        Long userId = UserContext.getUserId();
        return Result.ok(recommendService.recommend(userId, Math.min(limit, 30), seed));
    }

    /**
     * 商品关联推荐（"看了这个的人也在看"）
     * @param goodsId 当前商品ID
     * @param limit   推荐数量（默认6）
     */
    @GetMapping("/related")
    public Result<List<RecommendResultDTO>> related(@RequestParam Long goodsId,
                                                     @RequestParam(defaultValue = "6") int limit) {
        return Result.ok(recommendService.relatedGoods(goodsId, limit));
    }

    /**
     * 记录用户对推荐结果的反馈（点击/忽略）
     */
    @PostMapping("/feedback")
    public Result<Void> feedback(@RequestParam Long goodsId,
                                  @RequestParam(defaultValue = "5") int behaviorType) {
        Long userId = UserContext.getUserId();
        if (userId != null) {
            recommendService.logBehavior(userId, goodsId, behaviorType);
        }
        return Result.ok();
    }
}
