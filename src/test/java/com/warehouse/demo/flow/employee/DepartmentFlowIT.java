package com.warehouse.demo.flow.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.warehouse.demo.AbstractIT;

public class DepartmentFlowIT extends AbstractIT {
    private static final String MAJOR_HR = "13000001";

    @Test 
    void createReadAndDeleteDepartment() {
        String token = tokenFor(MAJOR_HR, TEST_PASSWORD);
        String name = "Test" + System.nanoTime() + " Department";

        Map<?, ?> created = client.post()
            .uri("/departments")
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("name", name, "priority", 25))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Map.class)
            .returnResult()
            .getResponseBody();

        long id = ((Number) created.get("id")).longValue();
        assertEquals(name.replace(' ', '_').toUpperCase(), created.get("codeName"));

        client.delete()
            .uri("/departments/{id}", id)
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isOk();

        client.get()
            .uri("/departments/{id}", id)
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test 
    void seededDepartmentCannotBeDeleted() {
        String token = tokenFor(MAJOR_HR, TEST_PASSWORD);
        long hrDepartmentId = getIdByName("/departments", "HR Department", token);

        client.delete()
            .uri("/departments/{id}", hrDepartmentId)
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        client.get()
            .uri("/departments/{id}", hrDepartmentId)
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isOk();
    }
}
