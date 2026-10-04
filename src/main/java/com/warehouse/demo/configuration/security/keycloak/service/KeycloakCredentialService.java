package com.warehouse.demo.configuration.security.keycloak.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakCredential;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class KeycloakCredentialService {
    private final RestClient keycloakRestClient;

    @Value("${warehouse.shared-password}")
    private String sharedPassword;

    public void setCredential(
        String userId
    ) {
        KeycloakCredential keycloakCredential = new KeycloakCredential(
            "password", 
            sharedPassword, 
            true
        );

        keycloakRestClient.put()
            .uri("/users/{userId}/reset-password", userId)
            .body(keycloakCredential)
            .retrieve()
            .toBodilessEntity();
    }

    public void clearRequiredActions(
        String userId
    ) {
        Map<String, List<String>> body = Map.of(
            "requiredActions", List.of()
        );

        keycloakRestClient.put()
            .uri("/users/{userId}", userId)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }
}
