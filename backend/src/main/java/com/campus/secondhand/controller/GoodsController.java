package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.GoodsDTO;
import com.campus.secondhand.dto.GoodsQueryDTO;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.GoodsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @PostMapping
    public Result<Goods> publish(@Valid @RequestBody GoodsDTO dto) {
        return Result.ok(goodsService.publish(dto, UserContext.getUserId()));
    }

    @PutMapping("/{id}")
    public Result<Goods> update(@PathVariable Long id, @Valid @RequestBody GoodsDTO dto) {
        return Result.ok(goodsService.updateGoods(id, dto, UserContext.getUserId()));
    }

    @PutMapping("/{id}/off")
    public Result<Void> offShelf(@PathVariable Long id) {
        if (UserContext.isAdmin()) {
            goodsService.adminOffShelf(id);
        } else {
            goodsService.offShelf(id, UserContext.getUserId());
        }
        return Result.ok();
    }

    @PutMapping("/{id}/on")
    public Result<Void> onShelf(@PathVariable Long id) {
        // Only owner can relist their own goods
        goodsService.onShelf(id, UserContext.getUserId());
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<Goods> detail(@PathVariable Long id) {
        return Result.ok(goodsService.getDetail(id));
    }

    @GetMapping("/search")
    public Result<Page<Goods>> search(GoodsQueryDTO query) {
        return Result.ok(goodsService.search(query));
    }

    @GetMapping("/my")
    public Result<Page<Goods>> myGoods(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(goodsService.myGoods(UserContext.getUserId(), page, pageSize));
    }
}
