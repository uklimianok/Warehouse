package com.warehouse.demo.configuration.security.keycloak.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeycloakUserRequest(
    String username, 
    boolean enabled, 
    String firstName, 
    String lastName, 
    List<KeycloakCredential> credentials
) {}
