package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.GoodsDTO;
import com.campus.secondhand.dto.GoodsQueryDTO;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final ObjectMapper objectMapper;

    @Override
    public Goods publish(GoodsDTO dto, Long userId) {
        Goods goods = buildGoods(dto);
        goods.setUserId(userId);
        goods.setStatus(1);
        goods.setViewCount(0);
        save(goods);
        return goods;
    }

    @Override
    public Goods updateGoods(Long id, GoodsDTO dto, Long userId) {
        Goods goods = getById(id);
        if (goods == null) throw new BusinessException("商品不存在");
        if (!goods.getUserId().equals(userId)) throw new BusinessException("无权操作");
        if (goods.getStatus() == 2) throw new BusinessException("已售商品不可编辑");

        Goods updated = buildGoods(dto);
        updated.setId(id);
        updated.setUserId(userId);
        updated.setStatus(goods.getStatus());
        updated.setViewCount(goods.getViewCount());
        updateById(updated);
        return getById(id);
    }

    @Override
    public void offShelf(Long id, Long userId) {
        Goods goods = getById(id);
        if (goods == null) throw new BusinessException("商品不存在");
        if (!goods.getUserId().equals(userId)) throw new BusinessException("无权操作");
        goods.setStatus(0);
        updateById(goods);
    }

    @Override
    public Goods getDetail(Long id) {
        Goods goods = getById(id);
        if (goods == null) throw new BusinessException("商品不存在");
        // Increment view count
        goods.setViewCount(goods.getViewCount() + 1);
        updateById(goods);
        return goods;
    }

    @Override
    public Page<Goods> search(GoodsQueryDTO query) {
        Page<Goods> page = new Page<>(query.getPage(), query.getPageSize());
        return baseMapper.selectGoodsPage(page, query);
    }

    @Override
    public Page<Goods> myGoods(Long userId, int page, int pageSize) {
        Page<Goods> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>()
                .eq(Goods::getUserId, userId)
                .orderByDesc(Goods::getCreatedAt);
        return page(p, wrapper);
    }

    private Goods buildGoods(GoodsDTO dto) {
        Goods goods = new Goods();
        goods.setTitle(dto.getTitle());
        goods.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        goods.setPrice(dto.getPrice());
        goods.setOriginalPrice(dto.getOriginalPrice());
        goods.setConditionLevel(dto.getConditionLevel() != null ? dto.getConditionLevel() : 3);
        goods.setCategory(dto.getCategory() != null ? dto.getCategory() : "other");
        try {
            List<String> images = dto.getImages();
            goods.setImages(images != null ? objectMapper.writeValueAsString(images) : "[]");
        } catch (JsonProcessingException e) {
            goods.setImages("[]");
        }
        return goods;
    }
}
