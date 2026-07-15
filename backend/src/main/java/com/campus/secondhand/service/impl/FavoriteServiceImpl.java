package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.entity.Favorite;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.mapper.FavoriteMapper;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    private final GoodsMapper goodsMapper;
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
    public Page<Favorite> myFavorites(Long userId, int page, int pageSize) {
        Page<Favorite> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreatedAt);
        return page(p, wrapper);
    }
}
