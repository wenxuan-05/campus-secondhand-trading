package com.campus.secondhand.config;

import com.campus.secondhand.service.OrderService;
import com.campus.secondhand.service.ReviewService;
import com.campus.secondhand.service.RefundService;
import com.campus.secondhand.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {

    private final ReviewService reviewService;
    private final RefundService refundService;
    private final OrderService orderService;
    private final UserService userService;

    /** Daily at 2 AM: auto 5-star review for orders completed >7 days ago */
    @Scheduled(cron = "0 0 2 * * ?")
    public void autoReviewExpiredOrders() {
        log.info("Starting auto-review task...");
        int count = reviewService.autoReviewExpiredOrders();
        log.info("Auto-review completed: {} reviews generated", count);
    }

    /** Every hour: escalate unhandled refunds older than 48h to platform arbitration */
    @Scheduled(cron = "0 0 * * * ?")
    public void autoEscalateRefunds() {
        log.info("Starting auto-escalate refunds task...");
        int count = refundService.autoEscalateUnhandled();
        if (count > 0) {
            log.info("Auto-escalated {} refund(s) to platform arbitration", count);
        }
    }

    /** Every 30 minutes: cancel unpaid orders (timeout 30 min) */
    @Scheduled(cron = "0 */30 * * * ?")
    public void autoCancelUnpaidOrders() {
        int count = orderService.autoCancelUnpaidOrders();
        if (count > 0) {
            log.info("Auto-cancelled {} unpaid order(s)", count);
        }
    }

    /** Every hour: auto-complete unconfirmed orders (48h after pickup) */
    @Scheduled(cron = "0 0 * * * ?")
    public void autoConfirmOrders() {
        int count = orderService.autoConfirmOrders();
        if (count > 0) {
            log.info("Auto-confirmed {} order(s)", count);
        }
    }

    /** Daily at 3 AM: auto-off-shelf goods expired after 30 days */
    @Scheduled(cron = "0 0 3 * * ?")
    public void autoOffShelfExpiredGoods() {
        int count = orderService.autoOffShelfExpiredGoods();
        if (count > 0) {
            log.info("Auto-off-shelf {} expired good(s)", count);
        }
    }

    /** Every hour: unban users whose ban period (7 days) has expired */
    @Scheduled(cron = "0 30 * * * ?")
    public void autoUnbanExpiredUsers() {
        int count = userService.unbanExpiredUsers();
        if (count > 0) {
            log.info("Unbanned {} user(s) with expired bans", count);
        }
    }
}
