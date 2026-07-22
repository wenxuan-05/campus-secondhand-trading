package com.campus.secondhand.service;

import com.campus.secondhand.dto.RecommendResultDTO;
import com.campus.secondhand.entity.Goods;

import java.util.List;

public interface RecommendService {
    /**
     * 为指定用户生成个性化推荐
     * @param userId 用户ID（null 则返回冷启动推荐）
     * @param limit  返回数量
     * @param seed   随机种子，非0时打乱候选集顺序（用于"换一批"）
     */
    List<RecommendResultDTO> recommend(Long userId, int limit, long seed);

    /**
     * 基于商品关联推荐（"看了这个的人也在看"）
     * @param goodsId 当前商品ID
     * @param limit   返回数量
     */
    List<RecommendResultDTO> relatedGoods(Long goodsId, int limit);

    /**
     * 对搜索结果进行个性化重排序
     * @param userId 用户ID
     * @param goodsList 原始搜索结果
     * @return 重排序后的结果
     */
    List<Goods> reRankPersonalized(Long userId, List<Goods> goodsList);

    /**
     * 记录用户行为
     * @param userId       用户ID
     * @param goodsId      商品ID
     * @param behaviorType 行为类型：1=浏览 2=收藏 3=购买 4=聊天 5=搜索点击
     */
    void logBehavior(Long userId, Long goodsId, int behaviorType);
}
