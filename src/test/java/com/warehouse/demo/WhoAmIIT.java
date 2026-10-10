package com.warehouse.demo;

import org.junit.jupiter.api.Test;

public class WhoAmIIT extends AbstractIT {
    @Test 
    void returnEmployeeNumberFromToken() {
        client.get()
            .uri("/whoami")
            .headers(h -> h.setBearerAuth(tokenFor("03000001")))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.username")
            .isEqualTo("03000001");
    }

    @Test 
    void rejectsRequestWithoutToken() {
        client.get()
            .uri("/whoami")
            .exchange()
            .expectStatus()
            .isUnauthorized();
    }
}
