package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.GoodsDTO;
import com.campus.secondhand.dto.GoodsQueryDTO;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    private static final int DAILY_PUBLISH_LIMIT = 10;
    private static final int MAX_ON_SALE = 50;
    private static final int RESTRICTED_DAILY_LIMIT = 3;
    private static final int DAILY_REFRESH_LIMIT = 3;

    @Override
    public Goods publish(GoodsDTO dto, Long userId) {
        // Check user status
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        if (user.getStatus() == 0) throw new BusinessException("账号已被禁用，无法发布商品");
        int dailyLimit = (user.getStatus() == 2) ? RESTRICTED_DAILY_LIMIT : DAILY_PUBLISH_LIMIT;

        // Check daily publish count
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        long todayCount = count(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getUserId, userId)
                .between(Goods::getCreatedAt, todayStart, todayEnd));
        if (todayCount >= dailyLimit) {
            throw new BusinessException("今日发布已达上限（" + dailyLimit + "条）");
        }

        // Check on-sale limit
        long onSaleCount = count(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getUserId, userId)
                .eq(Goods::getStatus, 2)); // 在售
        if (onSaleCount >= MAX_ON_SALE) {
            throw new BusinessException("在售商品已达上限（" + MAX_ON_SALE + "件）");
        }

        Goods goods = buildGoods(dto);
        goods.setUserId(userId);
        goods.setStatus(2); // 在售（跳过审核中状态）
        goods.setViewCount(0);
        goods.setCollectCount(0);
        goods.setChatCount(0);
        goods.setExpireTime(LocalDateTime.now().plusDays(30));
        save(goods);
        return goods;
    }

    @Override
    public Goods updateGoods(Long id, GoodsDTO dto, Long userId) {
        Goods goods = getById(id);
        if (goods == null) throw new BusinessException("商品不存在");
        if (!goods.getUserId().equals(userId)) throw new BusinessException("无权操作");
        if (goods.getStatus() == 3) throw new BusinessException("已售商品不可编辑");
        if (goods.getStatus() == 6) throw new BusinessException("已删除商品不可编辑");

        Goods updated = buildGoods(dto);
        updated.setId(id);
        updated.setUserId(userId);
        updated.setStatus(goods.getStatus());
        updated.setViewCount(goods.getViewCount());
        updated.setCollectCount(goods.getCollectCount());
        updated.setChatCount(goods.getChatCount());
        updated.setExpireTime(goods.getExpireTime());
        updateById(updated);
        return getById(id);
    }

    @Override
    public void offShelf(Long id, Long userId) {
        Goods goods = getById(id);
        if (goods == null) throw new BusinessException("商品不存在");
        if (!goods.getUserId().equals(userId)) throw new BusinessException("无权操作");
        if (goods.getStatus() != 2) throw new BusinessException("只有正在出售的商品可以下架");
        goods.setStatus(4); // 已下架
        updateById(goods);
    }

    @Override
    public void onShelf(Long id, Long userId) {
        Goods goods = getById(id);
        if (goods == null) throw new BusinessException("商品不存在");
        if (!goods.getUserId().equals(userId)) throw new BusinessException("无权操作");
        if (goods.getStatus() != 4) throw new BusinessException("只有已下架的商品可以上架");

        // Check daily refresh limit
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        long todayRefreshCount = count(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getUserId, userId)
                .between(Goods::getRefreshTime, todayStart, todayEnd));
        if (todayRefreshCount >= DAILY_REFRESH_LIMIT) {
            throw new BusinessException("每日刷新已达上限（" + DAILY_REFRESH_LIMIT + "次）");
        }

        goods.setStatus(2); // 在售
        goods.setRefreshTime(LocalDateTime.now());
        goods.setExpireTime(LocalDateTime.now().plusDays(30));
        updateById(goods);
    }

    @Override
    public void refreshGoods(Long id, Long userId) {
        Goods goods = getById(id);
        if (goods == null) throw new BusinessException("商品不存在");
        if (!goods.getUserId().equals(userId)) throw new BusinessException("无权操作");
        if (goods.getStatus() != 2) throw new BusinessException("只有在售商品可以刷新");

        // Check daily refresh limit
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        long todayRefreshCount = count(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getUserId, userId)
                .between(Goods::getRefreshTime, todayStart, todayEnd));
        if (todayRefreshCount >= DAILY_REFRESH_LIMIT) {
            throw new BusinessException("每日刷新已达上限（" + DAILY_REFRESH_LIMIT + "次）");
        }

        goods.setRefreshTime(LocalDateTime.now());
        updateById(goods);
    }

    @Override
    public void deleteGoods(Long id, Long userId) {
        Goods goods = getById(id);
        if (goods == null) throw new BusinessException("商品不存在");
        if (!goods.getUserId().equals(userId)) throw new BusinessException("无权操作");
        if (goods.getStatus() == 3) throw new BusinessException("有进行中的订单，不可删除");

        goods.setStatus(6); // 逻辑删除，保留30天
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
                .ne(Goods::getStatus, 6) // exclude deleted
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
