package com.warehouse.demo.configuration.security.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeycloakUserResponse(
    String id, 
    String username
) {}
