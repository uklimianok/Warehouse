package local.warehouse.app.configuration.security.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
public record KeycloakRole(
    String id,
    String name
) {}
