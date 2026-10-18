package local.warehouse.app.configuration.security.keycloak.dto;

public record KeycloakCredential(
    String type,
    String value,
    boolean temporary
) {}
