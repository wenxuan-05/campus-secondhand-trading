package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.dto.GoodsDTO;
import com.campus.secondhand.dto.GoodsQueryDTO;
import com.campus.secondhand.entity.Goods;

public interface GoodsService extends IService<Goods> {
    Goods publish(GoodsDTO dto, Long userId);
    Goods updateGoods(Long id, GoodsDTO dto, Long userId);
    void offShelf(Long id, Long userId);
    void onShelf(Long id, Long userId);
    void refreshGoods(Long id, Long userId);
    void deleteGoods(Long id, Long userId);
    Goods getDetail(Long id);
    Page<Goods> search(GoodsQueryDTO query);
    Page<Goods> myGoods(Long userId, int page, int pageSize);
}
