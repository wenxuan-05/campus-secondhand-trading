package com.campus.secondhand.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送验证码邮件。邮件发送失败时仅记录日志，不中断业务。
     * @param studentId 学号
     * @param code 4位验证码
     */
    public void sendVerificationCode(String studentId, String code) {
        String to = studentId + "@smail.swufe.edu.cn";
        // 始终在控制台打印验证码，方便开发调试
        log.info("=== 验证码：{} → {} ===", code, to);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("校园二手交易平台 淘鸭 - 验证码");
            message.setText("您的验证码是：" + code + "，5分钟内有效。如非本人操作，请忽略。");
            mailSender.send(message);
            log.info("验证码邮件已发送至 {}", to);
        } catch (Exception e) {
            log.error("邮件发送失败（验证码仍已生成，可从控制台查看）: {}", e.getMessage());
        }
    }
}
