package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.entity.Favorite;

public interface FavoriteService extends IService<Favorite> {
    /** Add a favorite, increments goods.collect_count */
    Favorite addFavorite(Long userId, Long goodsId);
    /** Remove a favorite, decrements goods.collect_count */
    void removeFavorite(Long userId, Long goodsId);
    /** Check if user has favorited this goods */
    boolean isFavorited(Long userId, Long goodsId);
    /** Get user's favorite list with pagination */
    Page<Favorite> myFavorites(Long userId, int page, int pageSize);
}
