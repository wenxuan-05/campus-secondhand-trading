package com.campus.secondhand.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * DFA (Deterministic Finite Automaton) based sensitive word filter.
 * Filters text content in O(n) time where n is the text length.
 */
@Slf4j
@Component
public class SensitiveWordFilter {

    private final TrieNode root = new TrieNode();

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd;
    }

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("sensitive-words.txt");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        addWord(line);
                        count++;
                    }
                }
                log.info("Loaded {} sensitive words", count);
            }
        } catch (Exception e) {
            log.warn("Failed to load sensitive-words.txt, using built-in word list. Error: {}",
                    e.getMessage());
            loadBuiltInWords();
        }
    }

    private void addWord(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEnd = true;
    }

    private void loadBuiltInWords() {
        // Fallback built-in list if file is not available
        String[] words = {
                "傻逼", "蠢货", "白痴", "去死", "妈的", "日你", "操你", "狗屎",
                "代考", "代写", "代课", "作弊", "答案", "刷单", "兼职刷单",
                "加微信", "加我微信", "扫码", "私聊", "加q", "加Q", "加扣",
                "枪手", "替考", "办证", "刻章", "发票代开", "套现", "网贷",
                "裸聊", "约炮", "小姐", "包夜", "上门服务"
        };
        for (String word : words) {
            addWord(word);
        }
        log.info("Loaded {} built-in sensitive words", words.length);
    }

    /**
     * Filter sensitive words in the text, replacing them with ***.
     */
    public String filter(String text) {
        if (text == null || text.isEmpty()) return text;

        StringBuilder result = new StringBuilder();
        int i = 0;
        int len = text.length();

        while (i < len) {
            TrieNode node = root;
            int matchEnd = -1;

            // Try to match from position i using DFA
            for (int j = i; j < len; j++) {
                char c = text.charAt(j);
                // Handle mixed case for English
                node = node.children.get(c);
                if (node == null) {
                    // Try case-insensitive for ASCII
                    if (c >= 'A' && c <= 'Z') {
                        node = root.children.get(Character.toLowerCase(c));
                    } else if (c >= 'a' && c <= 'z') {
                        node = root.children.get(Character.toUpperCase(c));
                    }
                    if (node == null) break;
                }
                if (node.isEnd) {
                    matchEnd = j;
                }
            }

            if (matchEnd >= 0) {
                // Replace matched range with ***
                for (int k = i; k <= matchEnd; k++) {
                    if (k == i || k == matchEnd || (k - i) % 3 == 0) {
                        result.append('*');
                    }
                }
                i = matchEnd + 1;
            } else {
                result.append(text.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    /**
     * Check if text contains any sensitive words.
     */
    public boolean containsSensitive(String text) {
        if (text == null || text.isEmpty()) return false;

        int len = text.length();
        for (int i = 0; i < len; i++) {
            TrieNode node = root;
            for (int j = i; j < len; j++) {
                char c = text.charAt(j);
                node = node.children.get(c);
                if (node == null) break;
                if (node.isEnd) return true;
            }
        }
        return false;
    }
}
