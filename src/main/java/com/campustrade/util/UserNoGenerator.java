package com.campustrade.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserNoGenerator {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static String lastTimestamp = "";

    public synchronized String generate() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        if (!timestamp.equals(lastTimestamp)) {
            counter.set(0);
            lastTimestamp = timestamp;
        }

        int seq = counter.incrementAndGet();
        return "U" + timestamp + String.format("%04d", seq);
    }
}