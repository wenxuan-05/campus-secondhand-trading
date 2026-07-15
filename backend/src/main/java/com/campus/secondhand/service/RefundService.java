package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.entity.Refund;

public interface RefundService extends IService<Refund> {
    /** Buyer applies for refund */
    Refund applyRefund(Long applicantId, Long orderId, String reason, String imagesJson);
    /** Seller approves refund */
    Refund approveRefund(Long sellerId, Long refundId);
    /** Seller rejects refund */
    Refund rejectRefund(Long sellerId, Long refundId);
    /** Admin arbitrates refund dispute */
    Refund arbitrate(Long refundId, int decision, String note);
    /** Auto-escalate unhandled refunds to platform arbitration (48h) */
    int autoEscalateUnhandled();
}
