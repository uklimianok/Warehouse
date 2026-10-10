package com.warehouse.demo;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ErrorIT extends AbstractIT {
    @Test 
    void unknownIdReturns404WithMessage() {
        client.get()
            .uri("/employees/999999")
            .headers(h -> h.setBearerAuth(tokenFor("24000001", TEST_PASSWORD)))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(String.class).isEqualTo("Employee not found.");
    }

    @Test 
    void invalidTokenReturns401() {
        client.get()
            .uri("/employees")
            .headers(h -> h.setBearerAuth("not-a-real-token"))
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test 
    void invalidBodyReturns400WithFieldErrors() {
        client.post()
            .uri("/employees")
            .headers(h -> h.setBearerAuth(tokenFor("13000001", TEST_PASSWORD)))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("lastName", "Doe"))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.firstName").exists()
            .jsonPath("$.positionId").exists();
    }

    @Test 
    void duplicateNameReturns409WithOwnMessage() {
        client.post()
            .uri("/departments")
            .headers(h -> h.setBearerAuth(tokenFor("13000001", TEST_PASSWORD)))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("name", "HR Department", "priority", 20))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectBody(String.class).isEqualTo("Department already exists.");
    }
}
