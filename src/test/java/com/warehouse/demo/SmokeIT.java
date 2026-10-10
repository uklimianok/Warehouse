package com.warehouse.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.warehouse.demo.configuration.security.keycloak.service.KeycloakRoleService;

public class SmokeIT extends AbstractIT {
    @Autowired RedisConnectionFactory redisConnectionFactory;
    @Autowired KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired KeycloakRoleService keycloakRoleService;

    @Test
    void contextLoads() {}

    @Test
    void redisResponds() {
        try (RedisConnection redisConnection = redisConnectionFactory.getConnection()) {
            assertEquals("PONG", redisConnection.ping());
        }
    }

    @Test
    void kafkaAcceptsMessages() throws Exception {
        kafkaTemplate.send("smoke-test", "ping").get(10, TimeUnit.SECONDS);
    }

    @Test
    void keycloakAdminApiWorks() {
        assertEquals("GOODS_PICKER", keycloakRoleService.readRole("GOODS_PICKER").name());
    }

    @Test 
    void warehouseEmployeeGetsTokenWithoutPassword() {
        assertFalse(tokenFor("03000001").isBlank());
    }

    @Test 
    void officeEmployeeGetsTokenWithPassword() {
        assertFalse(tokenFor("24000001", TEST_PASSWORD).isBlank());
    }
}
