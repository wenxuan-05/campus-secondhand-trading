package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.GoodsDTO;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.dto.GoodsQueryDTO;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.service.GoodsService;
import com.campus.secondhand.service.RecommendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;
    private final RecommendService recommendService;

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
        goods.setStatus(1); // 审核中（需校园大使/管理员审核通过后上架）
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

        goods.setStatus(1); // 审核中（重新上架需重新审核）
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
        // 记录浏览行为（用于推荐算法）
        Long currentUserId;
        try {
            currentUserId = UserContext.getUserId();
        } catch (Exception e) {
            currentUserId = null;
        }
        if (currentUserId != null) {
            recommendService.logBehavior(currentUserId, id, 1);
        }
        return goods;
    }

    @Override
    public Page<Goods> search(GoodsQueryDTO query) {
        Long userId = null;
        // Pass current user's dormitory for location-based sorting
        try {
            userId = UserContext.getUserId();
            if (userId != null && query.getDormitory() == null) {
                User user = userMapper.selectById(userId);
                if (user != null && user.getDormitory() != null && !user.getDormitory().isEmpty()) {
                    query.setDormitory(user.getDormitory());
                }
            }
        } catch (Exception ignored) {
            // User not logged in — no location boost
        }

        // 个性化排序：取更大的候选集，在内存中重排
        boolean isPersonalized = "personalized".equals(query.getSortBy());
        int fetchSize = isPersonalized ? Math.max(query.getPageSize(), 60) : query.getPageSize();
        Page<Goods> page = new Page<>(query.getPage(), fetchSize);

        // 个性化模式下用默认排序先取候选
        String originalSort = query.getSortBy();
        if (isPersonalized) {
            query.setSortBy(null); // 用默认 created_at DESC
        }

        Page<Goods> result = baseMapper.selectGoodsPage(page, query);

        if (isPersonalized && !result.getRecords().isEmpty()) {
            query.setSortBy(originalSort);
            List<Goods> reRanked = recommendService.reRankPersonalized(userId, result.getRecords());
            // 截取当前页
            int start = (query.getPage() - 1) * query.getPageSize();
            int end = Math.min(start + query.getPageSize(), reRanked.size());
            if (start < reRanked.size()) {
                result.setRecords(reRanked.subList(start, end));
            } else {
                result.setRecords(Collections.emptyList());
            }
        }

        return result;
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
        goods.setLocation(dto.getLocation() != null ? dto.getLocation() : "");
        try {
            List<String> images = dto.getImages();
            goods.setImages(images != null ? objectMapper.writeValueAsString(images) : "[]");
        } catch (JsonProcessingException e) {
            goods.setImages("[]");
        }
        return goods;
    }
}
