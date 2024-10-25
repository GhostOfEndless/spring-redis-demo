package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("redis")
public record RedisProperties(
        int chatConfigsDb,
        int usersStateDb,
        RedisInstance master,
        List<RedisInstance> slaves) {

    record RedisInstance(
            String host,
            int port
    ) {
    }
}
