package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.mapper.OrderMapper;
import com.campus.secondhand.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final GoodsMapper goodsMapper;

    @Override
    @Transactional
    public Order create(Long buyerId, Long goodsId, String remark) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException("商品不存在");
        if (goods.getStatus() != 1) throw new BusinessException("商品已下架或已售");
        if (goods.getUserId().equals(buyerId)) throw new BusinessException("不能购买自己的商品");

        // Mark goods as sold
        goods.setStatus(2);
        goodsMapper.updateById(goods);

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setBuyerId(buyerId);
        order.setSellerId(goods.getUserId());
        order.setGoodsId(goodsId);
        order.setPrice(goods.getPrice());
        order.setStatus(1); // pending
        order.setRemark(remark != null ? remark : "");
        save(order);
        return order;
    }

    @Override
    @Transactional
    public Order pay(Long orderId, Long buyerId) {
        Order order = getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getBuyerId().equals(buyerId)) throw new BusinessException("无权操作");
        if (order.getStatus() != 1) throw new BusinessException("订单状态异常，无法支付");

        // Mock payment — always succeed
        order.setStatus(2); // paid
        updateById(order);
        return order;
    }

    @Override
    public String generatePickupCode(Long orderId, Long sellerId) {
        Order order = getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getSellerId().equals(sellerId)) throw new BusinessException("无权操作");
        if (order.getStatus() != 2) throw new BusinessException("订单未付款，无法生成取货码");

        // Generate 6-digit code
        String code = String.format("%06d", new SecureRandom().nextInt(1000000));
        order.setPickupCode(code);
        order.setStatus(3); // pickup_ready
        updateById(order);
        return code;
    }

    @Override
    @Transactional
    public Order verifyPickup(Long orderId, Long sellerId, String code) {
        Order order = getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getSellerId().equals(sellerId)) throw new BusinessException("无权操作");
        if (order.getStatus() != 3) throw new BusinessException("订单状态异常");
        if (!code.equals(order.getPickupCode())) throw new BusinessException("取货码不匹配");

        order.setStatus(4); // completed
        updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order confirmReceive(Long orderId, Long buyerId) {
        Order order = getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getBuyerId().equals(buyerId)) throw new BusinessException("无权操作");
        if (order.getStatus() != 3) throw new BusinessException("当前状态无法确认收货");

        order.setStatus(4); // completed
        updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order cancel(Long orderId, Long userId) {
        Order order = getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getBuyerId().equals(userId) && !order.getSellerId().equals(userId)) {
            throw new BusinessException("无权操作");
        }
        if (order.getStatus() == 4) throw new BusinessException("已完成订单不可取消");
        if (order.getStatus() == 5) throw new BusinessException("订单已取消");

        // Restore goods to on-sale
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        if (goods != null && goods.getStatus() == 2) {
            goods.setStatus(1);
            goodsMapper.updateById(goods);
        }

        order.setStatus(5); // cancelled
        updateById(order);
        return order;
    }

    @Override
    public Page<Order> buyerOrders(Long buyerId, int page, int pageSize) {
        Page<Order> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getBuyerId, buyerId)
                .orderByDesc(Order::getCreatedAt);
        return page(p, wrapper);
    }

    @Override
    public Page<Order> sellerOrders(Long sellerId, int page, int pageSize) {
        Page<Order> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getSellerId, sellerId)
                .orderByDesc(Order::getCreatedAt);
        return page(p, wrapper);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new SecureRandom().nextInt(10000));
        return "CS" + timestamp + random;
    }
}
