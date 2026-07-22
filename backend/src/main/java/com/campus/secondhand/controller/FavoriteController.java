package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.FavoriteVO;
import com.campus.secondhand.entity.Favorite;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public Result<Favorite> add(@RequestBody Map<String, Long> body) {
        return Result.ok(favoriteService.addFavorite(UserContext.getUserId(), body.get("goodsId")));
    }

    @DeleteMapping("/{goodsId}")
    public Result<Void> remove(@PathVariable Long goodsId) {
        favoriteService.removeFavorite(UserContext.getUserId(), goodsId);
        return Result.ok();
    }

    @GetMapping("/check/{goodsId}")
    public Result<Map<String, Boolean>> check(@PathVariable Long goodsId) {
        return Result.ok(Map.of("favorited", favoriteService.isFavorited(UserContext.getUserId(), goodsId)));
    }

    @GetMapping
    public Result<Page<FavoriteVO>> myFavorites(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(favoriteService.myFavorites(UserContext.getUserId(), page, pageSize));
    }
}
