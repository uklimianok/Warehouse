package com.warehouse.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;

public class AccessIT extends AbstractIT {
    @ParameterizedTest(name = "{0} GET {1} -> {2}")
    @CsvFileSource(resources = "/access/read-access.csv", numLinesToSkip = 1)
    void readAccess(String employeeNumber, String url, int expectedStatus) {
        client.get()
            .uri(url)
            .headers(h -> h.setBearerAuth(tokenFor(employeeNumber, TEST_PASSWORD)))
            .exchange()
            .expectStatus().isEqualTo(expectedStatus);
    }

    @ParameterizedTest(name = "{0} POST {1} = {2}")
    @CsvFileSource(resources = "/access/create-access.csv", numLinesToSkip = 1)
    void createAccess(String employeeNumber, String url, int expectedStatus) {
        client.post()
            .uri(url)
            .headers(h -> h.setBearerAuth(tokenFor(employeeNumber, TEST_PASSWORD)))
            .contentType(MediaType.APPLICATION_JSON)
            .body(validBody(url))
            .exchange()
            .expectStatus().isEqualTo(expectedStatus);
    }

    @ParameterizedTest(name = "{0} cannot log in")
    @ValueSource(strings = {
        "01000001", "11000001", "16000001", "17000001",
        "18000001", "21000001", "22000001", "25000001"
    })
    void employeesWithoutDatabaseAccessCannotLogIn(String employeeNumber) {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> tokenFor(employeeNumber, TEST_PASSWORD));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getResponseBodyAsString().contains("invalid_grant"));
    }

    private Map<String, Object> validBody(String url) {
        String n = String.valueOf(System.nanoTime());
        return switch (url) {
            case "/gates" -> Map.of("symbol", "G" + n);
            case "/organization-types" -> Map.of("name", "O" + n);
            case "/workshops" -> Map.of("name", "W" + n, "standard", "10");
            default -> throw new IllegalArgumentException(url);
        };
    }
}
