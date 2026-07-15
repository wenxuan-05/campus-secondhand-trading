package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.Refund;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.mapper.OrderMapper;
import com.campus.secondhand.mapper.RefundMapper;
import com.campus.secondhand.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements RefundService {

    private final OrderMapper orderMapper;
    private final GoodsMapper goodsMapper;

    @Override
    @Transactional
    public Refund applyRefund(Long applicantId, Long orderId, String reason, String imagesJson) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getBuyerId().equals(applicantId)) throw new BusinessException("只有买家可以申请退款");
        if (order.getStatus() != 20 && order.getStatus() != 30) {
            throw new BusinessException("当前订单状态不支持退款");
        }

        // Check duplicate
        long count = count(new LambdaQueryWrapper<Refund>()
                .eq(Refund::getOrderId, orderId)
                .eq(Refund::getApplicantId, applicantId)
                .in(Refund::getStatus, 1, 4)); // pending or in arbitration
        if (count > 0) throw new BusinessException("已有进行中的退款申请");

        Refund refund = new Refund();
        refund.setOrderId(orderId);
        refund.setApplicantId(applicantId);
        refund.setReason(reason != null ? reason : "");
        refund.setImages(imagesJson != null ? imagesJson : "[]");
        refund.setAmount(order.getPrice());
        refund.setStatus(1); // 待卖家处理
        save(refund);

        // Update order status → 退款中
        order.setStatus(60);
        orderMapper.updateById(order);

        return refund;
    }

    @Override
    @Transactional
    public Refund approveRefund(Long sellerId, Long refundId) {
        Refund refund = getById(refundId);
        if (refund == null) throw new BusinessException("退款记录不存在");
        if (refund.getStatus() != 1) throw new BusinessException("退款状态异常，无法处理");

        Order order = orderMapper.selectById(refund.getOrderId());
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getSellerId().equals(sellerId)) throw new BusinessException("无权操作");

        refund.setStatus(2); // 卖家同意
        updateById(refund);

        // Update order → 已退款
        order.setStatus(80);
        orderMapper.updateById(order);

        // Restore goods to on-sale
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        if (goods != null && goods.getStatus() == 3) {
            goods.setStatus(2); // back to on-sale
            goodsMapper.updateById(goods);
        }

        return refund;
    }

    @Override
    @Transactional
    public Refund rejectRefund(Long sellerId, Long refundId) {
        Refund refund = getById(refundId);
        if (refund == null) throw new BusinessException("退款记录不存在");
        if (refund.getStatus() != 1) throw new BusinessException("退款状态异常，无法处理");

        Order order = orderMapper.selectById(refund.getOrderId());
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getSellerId().equals(sellerId)) throw new BusinessException("无权操作");

        refund.setStatus(3); // 卖家拒绝
        updateById(refund);

        // Update order → 退款纠纷
        order.setStatus(70);
        orderMapper.updateById(order);

        return refund;
    }

    @Override
    @Transactional
    public Refund arbitrate(Long refundId, int decision, String note) {
        Refund refund = getById(refundId);
        if (refund == null) throw new BusinessException("退款记录不存在");
        if (refund.getStatus() != 4 && refund.getStatus() != 3) {
            throw new BusinessException("退款状态异常，无法仲裁");
        }

        Order order = orderMapper.selectById(refund.getOrderId());
        if (order == null) throw new BusinessException("订单不存在");

        if (decision == 1) {
            // Support buyer → refund
            refund.setStatus(5); // 退款完成
            order.setStatus(80); // 已退款
            // Restore goods
            Goods goods = goodsMapper.selectById(order.getGoodsId());
            if (goods != null && goods.getStatus() == 3) {
                goods.setStatus(2);
                goodsMapper.updateById(goods);
            }
        } else {
            // Support seller → reject
            refund.setStatus(6); // 驳回
            order.setStatus(30); // 回到待确认状态
        }
        refund.setAdminNote(note);
        updateById(refund);
        orderMapper.updateById(order);

        return refund;
    }

    @Override
    @Transactional
    public int autoEscalateUnhandled() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusHours(48);
        var refunds = list(new LambdaQueryWrapper<Refund>()
                .eq(Refund::getStatus, 1) // pending
                .le(Refund::getCreatedAt, twoDaysAgo));

        int count = 0;
        for (Refund refund : refunds) {
            refund.setStatus(4); // escalate to platform arbitration
            updateById(refund);

            Order order = orderMapper.selectById(refund.getOrderId());
            if (order != null && order.getStatus() == 60) {
                order.setStatus(70); // 退款纠纷
                orderMapper.updateById(order);
            }
            count++;
        }
        return count;
    }
}
