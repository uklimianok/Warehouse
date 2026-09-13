package com.warehouse.demo.util.test.redis;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class RedisRunner implements CommandLineRunner {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        redisTemplate.opsForValue().set("launch-redis-message-key", "Redis is running ...");
        String result = redisTemplate.opsForValue().get("launch-redis-message-key");
        
        System.out.println(LocalDateTime.now() + " " + result);
    }
}
