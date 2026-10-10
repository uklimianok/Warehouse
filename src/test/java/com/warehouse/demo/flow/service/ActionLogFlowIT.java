package com.warehouse.demo.flow.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import com.warehouse.demo.AbstractIT;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActionLogFlowIT extends AbstractIT {
    private static final String SYSTEM_ADMINISTRATOR = "24000001";

    @Test 
    void createIsLoggedThroughKafka() {
        String token = tokenFor(SYSTEM_ADMINISTRATOR, TEST_PASSWORD);

        Map<?, ?> gate = client.post()
            .uri("/gates")
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("symbol", "G" + System.nanoTime()))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Map.class).returnResult().getResponseBody();
        long gateId = ((Number) gate.get("id")).longValue();

        await().atMost(Duration.ofSeconds(15)).untilAsserted(() -> {
            List<Map<String, Object>> logs = client.get()
                .uri("/action-logs")
                .headers(h -> h.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .returnResult().getResponseBody();

            assertTrue(logs.stream()
                .anyMatch(log -> 
                    "Gate".equals(log.get("entityType")) 
                    && ((Number) log.get("entityId")).longValue() == gateId 
                    && "create".equals(log.get("action")) 
                    && SYSTEM_ADMINISTRATOR.equals(log.get("employeeNumber"))
                )
            );
        });
    }
}
