package com.example.service;

import com.example.entity.ChatConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatConfigService {

    private final RedisTemplate<String, ChatConfig> redisTemplate;
    private final RedisMessageListenerContainer listenerContainer;
    private final Map<Long, ChatConfig> localCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        listenerContainer.addMessageListener(
                (message, pattern) -> {
                    String chatId = new String(message.getBody());
                    updateLocalConfig(chatId);
                },
                new ChannelTopic("configUpdateNotificationChannel")
        );
    }

    public ChatConfig getChatConfig(Long chatId) {
        return localCache.computeIfAbsent(chatId, this::fetchFromRedis);
    }

    private ChatConfig fetchFromRedis(Long chatId) {
        String key = "chat:" + chatId;
        return redisTemplate.opsForValue().get(key);
    }

    private void updateLocalConfig(String chatId) {
        ChatConfig updatedConfig = redisTemplate.opsForValue().get(chatId);
        log.info("Config updated! {}", updatedConfig);
        if (updatedConfig != null) {
            localCache.put(updatedConfig.getChatId(), updatedConfig);
        }
    }
}
