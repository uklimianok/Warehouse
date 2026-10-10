package com.warehouse.demo.flow.employee;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;

import com.warehouse.demo.AbstractIT;

public class EmployeeFlowIT extends AbstractIT {
    private static final String MAJOR_HR = "13000001";
    private static final String WAREHOUSE_HR = "14000001";

    @Test
    void employeeLifecycleIsMirroredInKeycloak() {
        String hrToken = tokenFor(MAJOR_HR, TEST_PASSWORD);

        // 1. HR creates a goods picker
        Map<?, ?> created = client.post().uri("/employees")
                .headers(h -> h.setBearerAuth(hrToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "firstName", "Test",
                        "lastName", "Picker",
                        "employerOrganizationId", getIdByName("/organizations", "Organization A", hrToken),
                        "positionId", getIdByName("/positions", "Goods Picker", hrToken),
                        "shiftId", getIdBy("/shifts", "symbol", "1", hrToken),
                        "birthDate", "1990-01-01",
                        "documentId", "AB123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Map.class).returnResult().getResponseBody();

        long id = ((Number) created.get("id")).longValue();
        String employeeNumber = (String) created.get("employeeNumber");

        // 2. The new employee can log in (no password: warehouse department) and work
        String pickerToken = tokenFor(employeeNumber);
        client.get().uri("/products")
                .headers(h -> h.setBearerAuth(pickerToken))
                .exchange()
                .expectStatus().isOk();

        // 3. HR deletes the employee
        client.delete().uri("/employees/{id}", id)
                .headers(h -> h.setBearerAuth(hrToken))
                .exchange()
                .expectStatus().isOk();

        // 4. No new login possible: the Keycloak user is gone
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class,
                () -> tokenFor(employeeNumber));
        assertTrue(exception.getResponseBodyAsString().contains("invalid_grant"));

        // 5. E6: the OLD token is still a valid JWT, but the employee no longer exists in the database
        client.get().uri("/products")
                .headers(h -> h.setBearerAuth(pickerToken))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test 
    void positionChangeTakesEffectWithOldToken() {
        String hrToken = tokenFor(MAJOR_HR, TEST_PASSWORD);

        // 1. Create an employee
        Map<?, ?> created = createEmployee("Goods Picker", hrToken);
        long id = ((Number) created.get("id")).longValue();
        String employeeNumber = (String) created.get("employeeNumber");

        // 2. Make a request as the new employee
        String oldToken = tokenFor(employeeNumber);
        client.get()
            .uri("/products")
            .headers(h -> h.setBearerAuth(oldToken))
            .exchange()
            .expectStatus().isOk();

        // 3. Change the employee's position
        client.put()
            .uri("/employees/{id}", id)
            .headers(h -> h.setBearerAuth(hrToken))
            .contentType(MediaType.APPLICATION_JSON)
            .body(employeeBody("Set Goods Loader", hrToken))
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.position.name").isEqualTo("Set Goods Loader");

        // 4. Make a request as the employee with new position on unable endpoint
        client.get()
            .uri("/products")
            .headers(h -> h.setBearerAuth(oldToken))
            .exchange()
            .expectStatus().isForbidden();

        // 5. Make a request as the employee on accessible endpoint
        client.get()
            .uri("/pallets")
            .headers(h -> h.setBearerAuth(oldToken))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void hrCannotAssignHigherRankedPosition() {
        String majorHrToken = tokenFor(MAJOR_HR, TEST_PASSWORD);
        String hrToken = tokenFor(WAREHOUSE_HR, TEST_PASSWORD);
        long pickerId = ((Number) createEmployee("Goods Picker", majorHrToken).get("id")).longValue();

        client.put()
            .uri("/employees/{id}", pickerId)
            .headers(h -> h.setBearerAuth(hrToken))
            .contentType(MediaType.APPLICATION_JSON)
            .body(employeeBody("System Administrator", hrToken))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test 
    void hrCannotEditHigherRankedEmployee() {
        String hrToken = tokenFor(WAREHOUSE_HR, TEST_PASSWORD);
        long directorId = getIdBy("/employees", "employeeNumber", "12000001", hrToken);

        client.put()
            .uri("/employees/{id}", directorId)
            .headers(h -> h.setBearerAuth(hrToken))
            .contentType(MediaType.APPLICATION_JSON)
            .body(employeeBody("Director", hrToken))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test 
    void warehouseEmployeeCannotEditOthers() {
        String majorHrToken = tokenFor(MAJOR_HR, TEST_PASSWORD);
        Map<?, ?> a = createEmployee("Goods Picker", majorHrToken);
        Map<?, ?> b = createEmployee("Goods Picker", majorHrToken);
        String tokenA = tokenFor((String) a.get("employeeNumber"));

        client.put()
            .uri("/employees/{id}", ((Number) b.get("id")).longValue())
            .headers(h -> h.setBearerAuth(tokenA))
            .contentType(MediaType.APPLICATION_JSON)
            .body(employeeBody("Goods Picker", majorHrToken))
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test 
    void warehouseEmployeeCanEditSelf() {
        String majorHrToken = tokenFor(MAJOR_HR, TEST_PASSWORD);
        Map<?, ?> newEmployee = createEmployee("Goods Picker", majorHrToken);
        String newEmployeeToken = tokenFor((String) newEmployee.get("employeeNumber"));

        client.put()
            .uri("/employees/{id}", ((Number) newEmployee.get("id")).longValue())
            .headers(h -> h.setBearerAuth(newEmployeeToken))
            .contentType(MediaType.APPLICATION_JSON)
            .body(employeeBody("Operator", majorHrToken))
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.position.name").isEqualTo("Operator");
    }

    @Test 
    void warehouseEmployeeCannotPromoteHimselfToAdmin() {
        String sysAdminToken = tokenFor("24000001", TEST_PASSWORD);
        String goodsPickerToken = tokenFor("03000001");

        long goodsPickerId = getIdBy("/employees", "employeeNumber", "03000001", sysAdminToken);

        client.put()
            .uri("/employees/{id}", goodsPickerId)
            .headers(h -> h.setBearerAuth(goodsPickerToken))
            .contentType(MediaType.APPLICATION_JSON)
            .body(employeeBody("System Administrator", sysAdminToken))
            .exchange()
            .expectStatus().isForbidden();

        long goodsPickerPositionId = getIdBy("/positions", "codeName", "GOODS_PICKER", sysAdminToken);

        client.get()
            .uri("/employees/{id}", goodsPickerId)
            .headers(h -> h.setBearerAuth(sysAdminToken))
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.position.id").isEqualTo(goodsPickerPositionId);
    }

    private Map<String, Object> employeeBody(String positionName, String token) {
        return Map.of(
            "firstName", "Test",
            "lastName", "Picker",
            "employerOrganizationId", getIdByName("/organizations", "Organization A", token),
            "positionId", getIdByName("/positions", positionName, token),
            "shiftId", getIdBy("/shifts", "symbol", "1", token),
            "birthDate", "1990-01-01",
            "documentId", "AB123456"
        );
    }

    private Map<?, ?> createEmployee(String positionName, String token) {
        return client.post()
            .uri("/employees")
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(employeeBody(positionName, token))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Map.class).returnResult().getResponseBody();
    }
}
