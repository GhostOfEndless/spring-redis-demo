package com.example.config;

import com.example.entity.ChatConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean(name = "redisConnectionFactoryConfigDB")
    public LettuceConnectionFactory lettuceConnectionFactoryConfigDB() {
        return createLettuceConnectionFactory(1);
    }

    @Bean(name = "redisConnectionFactoryUserDB")
    public LettuceConnectionFactory lettuceConnectionFactoryUserDB() {
        return createLettuceConnectionFactory(2);
    }

    @Bean
    public RedisTemplate<String, ChatConfig> redisTemplate() {
        var template = new RedisTemplate<String, ChatConfig>();
        template.setConnectionFactory(lettuceConnectionFactoryConfigDB());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatConfig.class));
        template.setKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(lettuceConnectionFactoryConfigDB());
    }

    private LettuceConnectionFactory createLettuceConnectionFactory(int database) {
        log.info("Redis host {} port {} db {}", host, port, database);
        var redisStandaloneConfig = new RedisStandaloneConfiguration(host, port);
        redisStandaloneConfig.setDatabase(database);

        return new LettuceConnectionFactory(redisStandaloneConfig);
    }
}
