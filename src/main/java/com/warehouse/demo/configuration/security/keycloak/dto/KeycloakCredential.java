package com.warehouse.demo.configuration.security.keycloak.dto;

public record KeycloakCredential(
    String type,
    String value,
    boolean temporary
) {}
