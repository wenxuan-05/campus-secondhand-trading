package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.BuyRequestDTO;
import com.campus.secondhand.dto.BuyRequestQueryDTO;
import com.campus.secondhand.entity.BuyRequest;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.BuyRequestMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.service.BuyRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class BuyRequestServiceImpl extends ServiceImpl<BuyRequestMapper, BuyRequest> implements BuyRequestService {

    private final UserMapper userMapper;

    private static final int DAILY_PUBLISH_LIMIT = 5;

    @Override
    public BuyRequest publish(BuyRequestDTO dto, Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        if (user.getStatus() == 0) throw new BusinessException("账号已被禁用，无法发布求购");

        // Check daily limit
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        long todayCount = count(new LambdaQueryWrapper<BuyRequest>()
                .eq(BuyRequest::getUserId, userId)
                .between(BuyRequest::getCreatedAt, todayStart, todayEnd));
        if (todayCount >= DAILY_PUBLISH_LIMIT) {
            throw new BusinessException("今日发布已达上限（" + DAILY_PUBLISH_LIMIT + "条）");
        }

        BuyRequest br = new BuyRequest();
        br.setUserId(userId);
        br.setTitle(dto.getTitle());
        br.setCategory(dto.getCategory() != null ? dto.getCategory() : "other");
        br.setBudget(dto.getBudget());
        br.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        br.setStatus(1); // 发布中
        br.setViewCount(0);
        save(br);
        return br;
    }

    @Override
    public void cancel(Long id, Long userId) {
        BuyRequest br = getById(id);
        if (br == null) throw new BusinessException("求购信息不存在");
        if (!br.getUserId().equals(userId)) throw new BusinessException("只能撤销自己的求购");
        if (br.getStatus() == 4) throw new BusinessException("该求购已撤销");
        if (br.getStatus() == 3) throw new BusinessException("已成交的求购不可撤销");
        br.setStatus(4); // 已撤销
        updateById(br);
    }

    @Override
    public Page<BuyRequest> myRequests(Long userId, int page, int pageSize) {
        Page<BuyRequest> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<BuyRequest> wrapper = new LambdaQueryWrapper<BuyRequest>()
                .eq(BuyRequest::getUserId, userId)
                .orderByDesc(BuyRequest::getCreatedAt);
        return page(p, wrapper);
    }

    @Override
    public Page<BuyRequest> search(BuyRequestQueryDTO query) {
        Page<BuyRequest> p = new Page<>(query.getPage(), query.getPageSize());
        LambdaQueryWrapper<BuyRequest> wrapper = new LambdaQueryWrapper<BuyRequest>();

        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.like(BuyRequest::getTitle, query.getKeyword());
        }
        if (StringUtils.hasText(query.getCategory())) {
            wrapper.eq(BuyRequest::getCategory, query.getCategory());
        }
        if (query.getStatus() != null) {
            wrapper.eq(BuyRequest::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(BuyRequest::getCreatedAt);
        return page(p, wrapper);
    }

    @Override
    public BuyRequest getDetail(Long id) {
        BuyRequest br = getById(id);
        if (br == null) throw new BusinessException("求购信息不存在");
        br.setViewCount(br.getViewCount() + 1);
        updateById(br);
        return br;
    }
}
