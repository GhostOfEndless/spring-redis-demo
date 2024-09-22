package com.example.service;

import com.example.entity.ChatConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatConfigService {

    private final RedisTemplate<String, ChatConfig> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        if (getChatConfig(1L) == null) {
            updateChatConfig(ChatConfig.builder()
                    .chatId(1L)
                    .enableAllowedPremiumEmojiSets(false)
                    .isBlockPremiumEmoji(false)
                    .allowedPremiumEmojiSets(List.of())
                    .build());
        }
        log.info("Current config is: {}", getChatConfig(1L));
    }

    public void updateChatConfig(ChatConfig chatConfig) {
        String key = "chat:" + chatConfig.getChatId();
        redisTemplate.opsForValue().set(key, chatConfig);
        stringRedisTemplate.convertAndSend("configUpdateNotificationChannel", key);
    }

    public ChatConfig getChatConfig(Long chatId) {
        String key = "chat:" + chatId;
        return redisTemplate.opsForValue().get(key);
    }
}
