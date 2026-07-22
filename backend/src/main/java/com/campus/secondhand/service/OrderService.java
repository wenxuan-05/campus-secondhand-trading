package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.entity.Order;

public interface OrderService extends IService<Order> {
    Order create(Long buyerId, Long goodsId, String remark);
    Order pay(Long orderId, Long buyerId);
    String generatePickupCode(Long orderId, Long sellerId);
    Order verifyPickup(Long orderId, Long buyerId, String code);
    Order confirmReceive(Long orderId, Long buyerId);
    Order cancel(Long orderId, Long userId);
    Page<Order> buyerOrders(Long buyerId, int page, int pageSize);
    Page<Order> sellerOrders(Long sellerId, int page, int pageSize);

    /** Scheduled tasks */
    int autoCancelUnpaidOrders();
    int autoConfirmOrders();
    int autoOffShelfExpiredGoods();
}
