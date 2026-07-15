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

    /**
     * Status codes (aligned with PDF 4.2.2):
     * 10=待付款, 20=待面交, 25=待发货, 30=待确认, 40=待评价
     * 50=已评价, 60=退款中, 70=退款纠纷, 80=已退款, 90=已取消, 100=已关闭
     */

    @Override
    @Transactional
    public Order create(Long buyerId, Long goodsId, String remark) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException("商品不存在");
        if (goods.getStatus() != 2) throw new BusinessException("商品已下架或已售");
        if (goods.getUserId().equals(buyerId)) throw new BusinessException("不能购买自己的商品");

        // Mark goods as sold (3=已售)
        goods.setStatus(3);
        goodsMapper.updateById(goods);

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setBuyerId(buyerId);
        order.setSellerId(goods.getUserId());
        order.setGoodsId(goodsId);
        order.setPrice(goods.getPrice());
        order.setStatus(10); // 待付款
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
        if (order.getStatus() != 10) throw new BusinessException("订单状态异常，无法支付");

        // Mock payment — always succeed
        order.setStatus(20); // 待面交
        order.setPayTime(LocalDateTime.now());
        updateById(order);
        return order;
    }

    @Override
    public String generatePickupCode(Long orderId, Long sellerId) {
        Order order = getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getSellerId().equals(sellerId)) throw new BusinessException("无权操作");
        if (order.getStatus() != 20) throw new BusinessException("订单未付款，无法生成取货码");

        // Generate 6-digit code (avoid sequential/repeated patterns)
        String code;
        do {
            code = String.format("%06d", new SecureRandom().nextInt(1000000));
        } while (isWeakCode(code));
        order.setPickupCode(code);
        updateById(order);
        return code;
    }

    private boolean isWeakCode(String code) {
        // Reject all-same digits and sequential digits
        if (code.matches("(\\d)\\1{5}")) return true;
        int d1 = code.charAt(0) - '0';
        int d2 = code.charAt(1) - '0';
        if (d2 == d1 + 1 && code.charAt(2) - '0' == d2 + 1) return true;
        return false;
    }

    @Override
    @Transactional
    public Order verifyPickup(Long orderId, Long sellerId, String code) {
        Order order = getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getSellerId().equals(sellerId)) throw new BusinessException("无权操作");
        if (order.getStatus() != 20) throw new BusinessException("订单状态异常");
        if (order.getPickupCode() == null || !order.getPickupCode().equals(code)) {
            throw new BusinessException("取货码不匹配");
        }

        order.setStatus(30); // 待确认
        order.setPickupTime(LocalDateTime.now());
        updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order confirmReceive(Long orderId, Long buyerId) {
        Order order = getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getBuyerId().equals(buyerId)) throw new BusinessException("无权操作");
        if (order.getStatus() != 30) throw new BusinessException("当前状态无法确认收货");

        order.setStatus(40); // 待评价
        order.setConfirmTime(LocalDateTime.now());
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
        if (order.getStatus() >= 40) throw new BusinessException("已完成订单不可取消");
        if (order.getStatus() == 90) throw new BusinessException("订单已取消");

        // Restore goods to on-sale (status 2)
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        if (goods != null && goods.getStatus() == 3) {
            goods.setStatus(2);
            goodsMapper.updateById(goods);
        }

        order.setStatus(90); // 已取消
        order.setCancelTime(LocalDateTime.now());
        if (userId.equals(order.getBuyerId())) {
            order.setCancelReason("买家取消");
        } else {
            order.setCancelReason("卖家取消");
        }
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

    /** Cancel unpaid orders after 30 minutes */
    @Override
    @Transactional
    public int autoCancelUnpaidOrders() {
        LocalDateTime thirtyMinAgo = LocalDateTime.now().minusMinutes(30);
        var orders = list(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, 10) // 待付款
                .le(Order::getCreatedAt, thirtyMinAgo));

        int count = 0;
        for (Order order : orders) {
            Goods goods = goodsMapper.selectById(order.getGoodsId());
            if (goods != null && goods.getStatus() == 3) {
                goods.setStatus(2); // restore to on-sale
                goodsMapper.updateById(goods);
            }
            order.setStatus(90); // 已取消
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason("支付超时自动取消");
            updateById(order);
            count++;
        }
        return count;
    }

    /** Auto-confirm orders 48 hours after pickup/verification */
    @Override
    @Transactional
    public int autoConfirmOrders() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusHours(48);
        var orders = list(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, 30) // 待确认
                .le(Order::getPickupTime, twoDaysAgo));

        int count = 0;
        for (Order order : orders) {
            order.setStatus(40); // 待评价
            order.setConfirmTime(LocalDateTime.now());
            updateById(order);
            count++;
        }
        return count;
    }

    /** Auto-off-shelf goods expired after 30 days */
    @Override
    @Transactional
    public int autoOffShelfExpiredGoods() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        var goodsList = goodsMapper.selectList(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getStatus, 2) // 在售
                .le(Goods::getCreatedAt, thirtyDaysAgo));

        int count = 0;
        for (Goods g : goodsList) {
            g.setStatus(4); // 已下架
            goodsMapper.updateById(g);
            count++;
        }
        return count;
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new SecureRandom().nextInt(10000));
        return "CS" + timestamp + random;
    }
}
