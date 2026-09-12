package com.warehouse.demo.configuration.redis;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration 
@EnableCaching 
@RequiredArgsConstructor 
public class RedisConfiguration {
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean 
    RedisCacheManager redisCacheManager() {
        RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration
            .defaultCacheConfig()
            .serializeValuesWith(SerializationPair
                .fromSerializer(GenericJacksonJsonRedisSerializer
                    .builder()
                    .enableDefaultTyping(BasicPolymorphicTypeValidator
                        .builder()
                        .allowIfSubType("com.warehouse.demo.entity")
                        .build()
                    )
                    .build()))
            .entryTtl(Duration.ofDays(1));

        RedisCacheConfiguration activeCacheConfiguration = RedisCacheConfiguration  // for Order-like entities
            .defaultCacheConfig()
            .serializeValuesWith(SerializationPair
                .fromSerializer(GenericJacksonJsonRedisSerializer
                    .builder()
                    .enableDefaultTyping(BasicPolymorphicTypeValidator
                        .builder()
                        .allowIfSubType("com.warehouse.demo.entity")
                        .build()
                    )
                    .build()))
            .entryTtl(Duration.ofMinutes(20));

        RedisCacheManager cacheManager = RedisCacheManager
            .builder(redisConnectionFactory)
            .withCacheConfiguration("orders", activeCacheConfiguration)
            .withCacheConfiguration("orderPallets", activeCacheConfiguration)
            .withCacheConfiguration("productPallets", activeCacheConfiguration)
            .cacheDefaults(defaultCacheConfiguration)
            .build();

        return cacheManager;
    }
}
