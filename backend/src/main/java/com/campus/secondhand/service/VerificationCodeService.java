package com.campus.secondhand.service;

import com.campus.secondhand.common.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class VerificationCodeService {

    // 验证码存储：key = type:studentId, value = code
    private final Map<String, CodeEntry> codeStore = new ConcurrentHashMap<>();
    // 频率限制：1分钟内
    private final Map<String, Long> limit1Min = new ConcurrentHashMap<>();
    // 频率限制：1小时内计数
    private final Map<String, HourCounter> limit1Hour = new ConcurrentHashMap<>();

    private static final int CODE_TTL = 5 * 60 * 1000; // 5分钟（毫秒）
    private static final int LIMIT_1MIN = 60 * 1000;    // 1分钟
    private static final int LIMIT_1HOUR = 60 * 60 * 1000; // 1小时
    private static final int MAX_PER_HOUR = 5;

    /**
     * 生成并存储验证码，返回code（不发送邮件）
     */
    public String generate(String studentId, String type) {
        checkRateLimit(studentId);
        String code = String.format("%04d", new Random().nextInt(10000));
        String key = type + ":" + studentId;
        codeStore.put(key, new CodeEntry(code, System.currentTimeMillis()));
        // 记录频率
        limit1Min.put(studentId, System.currentTimeMillis());
        HourCounter counter = limit1Hour.computeIfAbsent(studentId, k -> new HourCounter());
        counter.increment();
        log.info("=== 验证码生成：{} → {}@smail.swufe.edu.cn ===", code, studentId);
        return code;
    }

    /**
     * 校验验证码
     */
    public void verify(String studentId, String code, String type) {
        String key = type + ":" + studentId;
        CodeEntry entry = codeStore.get(key);
        if (entry == null) {
            throw new BusinessException("验证码已过期或未发送，请重新获取");
        }
        // 检查是否过期（5分钟）
        if (System.currentTimeMillis() - entry.createTime > CODE_TTL) {
            codeStore.remove(key);
            throw new BusinessException("验证码已过期，请重新获取");
        }
        if (!entry.code.equals(code)) {
            throw new BusinessException("验证码错误");
        }
        // 验证成功后删除
        codeStore.remove(key);
    }

    /**
     * 检查发送频率限制
     */
    private void checkRateLimit(String studentId) {
        // 1分钟内限制
        Long lastTime = limit1Min.get(studentId);
        if (lastTime != null && System.currentTimeMillis() - lastTime < LIMIT_1MIN) {
            long remainSec = (LIMIT_1MIN - (System.currentTimeMillis() - lastTime)) / 1000 + 1;
            throw new BusinessException("发送过于频繁，请" + remainSec + "秒后再试");
        }
        // 1小时内限制
        HourCounter counter = limit1Hour.get(studentId);
        if (counter != null) {
            // 清理超过1小时的计数
            if (System.currentTimeMillis() - counter.startTime > LIMIT_1HOUR) {
                limit1Hour.remove(studentId);
            } else if (counter.count >= MAX_PER_HOUR) {
                throw new BusinessException("该学号1小时内发送次数已达上限（5次），请稍后再试");
            }
        }
        // 清理过期验证码
        codeStore.entrySet().removeIf(e ->
                System.currentTimeMillis() - e.getValue().createTime > CODE_TTL);
    }

    private static class CodeEntry {
        final String code;
        final long createTime;
        CodeEntry(String code, long createTime) {
            this.code = code;
            this.createTime = createTime;
        }
    }

    private static class HourCounter {
        final long startTime = System.currentTimeMillis();
        int count = 0;
        void increment() { count++; }
    }
}
