package com.warehouse.demo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class LaunchRunner implements CommandLineRunner {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        redisTemplate.opsForValue().set("launch-redis-message-key", "The value from launch message for checking Redis");
        String result = redisTemplate.opsForValue().get("launch-redis-message-key");
        System.out.println("LaunchRunner: " + result);
    }
}
