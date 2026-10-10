package com.warehouse.demo.flow.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.HttpClientErrorException;

import com.warehouse.demo.AbstractIT;
import com.warehouse.demo.configuration.security.keycloak.service.KeycloakRoleService;

public class PositionFlowIT extends AbstractIT {
    private static final String MAJOR_HR = "13000001";

    @Autowired KeycloakRoleService keycloakRoleService;

    @Test 
    void createAndDeletePositionManagesKeycloakRole() {
        String token = tokenFor(MAJOR_HR, TEST_PASSWORD);
        String name = "Test" + System.nanoTime() + " Position";
        String codeName = name.replace(' ', '_').toUpperCase();
        long departmentId = getIdByName("/departments", "Warehouse Employees Department", token);

        Map<?, ?> created = client.post()
            .uri("/positions")
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of(
                "name", name,
                "enabled", false,
                "departmentId", departmentId,
                "inheritedPositionsId", List.of(),
                "controllerFlags", Map.of()
            ))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Map.class)
            .returnResult()
            .getResponseBody();

        long id = ((Number) created.get("id")).longValue();
        assertEquals(codeName, keycloakRoleService.readRole(codeName).name());

        client.delete()
            .uri("/positions/{id}", id)
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isOk();

        assertThrows(HttpClientErrorException.NotFound.class, () -> keycloakRoleService.readRole(codeName));
    }

    @Test 
    void inheritanceLoopIsRejected() {
        String token = tokenFor(MAJOR_HR, TEST_PASSWORD);
        String nameA = "TestA" + System.nanoTime() + " Position";
        String nameB = "TestB" + System.nanoTime() + " Position";

        long a = createPosition(nameA, List.of(), token);
        long b = createPosition(nameB, List.of(a), token);

        client.put()
            .uri("/positions/{id}", a)
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(positionBody(nameA, List.of(b), token))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectBody(String.class).isEqualTo("This position is already in the chain of position inheritance.");
    }

    @Test 
    void inheritedPositionCannotBeDeletedUntilHeirIsDeleted() {
        String token = tokenFor(MAJOR_HR, TEST_PASSWORD);
        long a = createPosition("TestA" + System.nanoTime() + " Position", List.of(), token);
        long b = createPosition("TestB" + System.nanoTime() + " Position", List.of(a), token);

        deletePosition(a, token)
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectBody(String.class).isEqualTo("Position is active.");

        deletePosition(b, token)
            .expectStatus().isOk();
        deletePosition(a, token)
            .expectStatus().isOk();
    }

    private long createPosition(String name, List<Long> inheritedIds, String token) {
        Map<?, ?> created = client.post()
            .uri("/positions")
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(positionBody(name, inheritedIds, token))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Map.class)
            .returnResult()
            .getResponseBody();

        return ((Number) created.get("id")).longValue();
    }

    private RestTestClient.ResponseSpec deletePosition(long id, String token) {
        return client.delete()
            .uri("/positions/{id}", id)
            .headers(h -> h.setBearerAuth(token))
            .exchange();
    }

    private Map<String, Object> positionBody(String name, List<Long> inheritedIds, String token) {
        return Map.of(
            "name", name,
            "enabled", false,
            "departmentId", getIdByName("/departments", "Warehouse Employees Department", token),
            "inheritedPositionsId", inheritedIds,
            "controllerFlags", Map.of()
        );
    }
}
