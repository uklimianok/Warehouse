package com.warehouse.demo.configuration.security.keycloak.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakRole;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class KeycloakRoleService {
    private final RestClient keycloakRestClient;

    public KeycloakRole readByName(String name) {
        ResponseEntity<KeycloakRole> result = keycloakRestClient.get()
            .uri("/roles/{name}", name)
            .retrieve()
            .toEntity(KeycloakRole.class);

        return result.getBody();
    }
}
