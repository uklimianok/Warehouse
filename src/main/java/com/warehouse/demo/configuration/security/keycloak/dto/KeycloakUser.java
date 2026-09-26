package com.warehouse.demo.configuration.security.keycloak.dto;

import java.util.List;

public record KeycloakUser(
    String username, 
    boolean enabled, 
    String firstName, 
    String lastName, 
    List<KeycloakCredential> credentials
) {}
