package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.FavoriteVO;
import com.campus.secondhand.entity.Favorite;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.mapper.FavoriteMapper;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.service.FavoriteService;
import com.campus.secondhand.service.RecommendService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    private final GoodsMapper goodsMapper;
    private final RecommendService recommendService;
    private static final int MAX_FAVORITES = 200;

    @Override
    @Transactional
    public Favorite addFavorite(Long userId, Long goodsId) {
        // Check goods exists
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException("商品不存在");

        // Check duplicate
        long count = count(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getGoodsId, goodsId));
        if (count > 0) throw new BusinessException("已收藏该商品");

        // Check max limit
        long total = count(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId));
        if (total >= MAX_FAVORITES) throw new BusinessException("收藏已达上限（200件）");

        // Save favorite
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setGoodsId(goodsId);
        save(favorite);

        // Increment goods collect_count
        if (goods.getCollectCount() == null) goods.setCollectCount(0);
        goods.setCollectCount(goods.getCollectCount() + 1);
        goodsMapper.updateById(goods);

        // 记录收藏行为（用于推荐算法）
        recommendService.logBehavior(userId, goodsId, 2);

        return favorite;
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long goodsId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getGoodsId, goodsId);
        Favorite favorite = getOne(wrapper);
        if (favorite == null) throw new BusinessException("未收藏该商品");

        removeById(favorite.getId());

        // Decrement goods collect_count
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods != null && goods.getCollectCount() != null && goods.getCollectCount() > 0) {
            goods.setCollectCount(goods.getCollectCount() - 1);
            goodsMapper.updateById(goods);
        }
    }

    @Override
    public boolean isFavorited(Long userId, Long goodsId) {
        return count(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getGoodsId, goodsId)) > 0;
    }

    @Override
    public Page<FavoriteVO> myFavorites(Long userId, int page, int pageSize) {
        Page<Favorite> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreatedAt);
        Page<Favorite> favPage = page(p, wrapper);

        List<Favorite> favorites = favPage.getRecords();
        if (favorites.isEmpty()) {
            Page<FavoriteVO> emptyPage = new Page<>(page, pageSize);
            emptyPage.setTotal(0);
            emptyPage.setRecords(Collections.emptyList());
            return emptyPage;
        }

        // Batch query goods info
        List<Long> goodsIds = favorites.stream()
                .map(Favorite::getGoodsId)
                .collect(Collectors.toList());
        List<Goods> goodsList = goodsMapper.selectBatchIds(goodsIds);
        Map<Long, Goods> goodsMap = goodsList.stream()
                .collect(Collectors.toMap(Goods::getId, g -> g));

        // Build FavoriteVO list
        ObjectMapper objectMapper = new ObjectMapper();
        List<FavoriteVO> voList = favorites.stream().map(f -> {
            FavoriteVO vo = new FavoriteVO();
            vo.setId(f.getId());
            vo.setUserId(f.getUserId());
            vo.setGoodsId(f.getGoodsId());
            vo.setCreatedAt(f.getCreatedAt());

            Goods g = goodsMap.get(f.getGoodsId());
            if (g != null) {
                vo.setGoodsTitle(g.getTitle());
                vo.setGoodsPrice(g.getPrice());
                vo.setGoodsStatus(g.getStatus());
                // Extract first image from images JSON array
                vo.setGoodsImage(extractFirstImage(g.getImages(), objectMapper));
            }
            return vo;
        }).collect(Collectors.toList());

        Page<FavoriteVO> voPage = new Page<>(page, pageSize);
        voPage.setTotal(favPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    private String extractFirstImage(String imagesJson, ObjectMapper objectMapper) {
        if (imagesJson == null || imagesJson.isBlank()) return null;
        try {
            JsonNode arr = objectMapper.readTree(imagesJson);
            if (arr.isArray() && arr.size() > 0) {
                return arr.get(0).asText();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
