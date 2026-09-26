package com.warehouse.demo.configuration.security.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore fields which aren't declared here
public record KeycloakRole(
    String id,
    String name
) {}
