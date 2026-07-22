package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.dto.BuyRequestDTO;
import com.campus.secondhand.dto.BuyRequestQueryDTO;
import com.campus.secondhand.entity.BuyRequest;

public interface BuyRequestService extends IService<BuyRequest> {
    BuyRequest publish(BuyRequestDTO dto, Long userId);
    void cancel(Long id, Long userId);
    Page<BuyRequest> myRequests(Long userId, int page, int pageSize);
    Page<BuyRequest> search(BuyRequestQueryDTO query);
    BuyRequest getDetail(Long id);
}
