package com.warehouse.demo.configuration.security.keycloak.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakRole;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.util.info.DepartmentCodeNames;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class KeycloakRoleService {
    private final RestClient keycloakRestClient;

    private final KeycloakCredentialService keycloakCredentialService;

    private static final String PASSWORD_REQUIRED_ROLE = "password_required";

    public KeycloakRole readRole(
        String roleName
    ) {
        ResponseEntity<KeycloakRole> response = keycloakRestClient.get()
            .uri("/roles/{roleName}", roleName)
            .retrieve()
            .toEntity(KeycloakRole.class);

        return response.getBody();
    }

    public KeycloakRole createRole(
        String roleName
    ) {
        ResponseEntity<KeycloakRole> response = keycloakRestClient.post()
            .uri("/roles")
            .body(new KeycloakRole(null, roleName))
            .retrieve()
            .toEntity(KeycloakRole.class);

        return response.getBody();
    }

    public void deleteRole(
        String roleName
    ) {
        keycloakRestClient.delete()
            .uri("/roles/{roleName}", roleName)
            .retrieve()
            .toBodilessEntity();
    }

    public void assignRolesAtUser(
        String userId, 
        List<KeycloakRole> roles
    ) {
        keycloakRestClient.post()
            .uri("/users/{id}/role-mappings/realm", userId)
            .body(roles)
            .retrieve()
            .toBodilessEntity();
    }

    public void updateRolesAtUser(
        String userId, 
        Position oldPosition, 
        Position newPosition
    ) {
        boolean passwordIsRequiredBefore = !oldPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.WAREHOUSE_EMPLOYEES_DEPARTMENT);
        boolean passwordIsRequiredAfter = !newPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.WAREHOUSE_EMPLOYEES_DEPARTMENT);

        List<KeycloakRole> keycloakRoles = new ArrayList<>();
        keycloakRoles.add(readRole(oldPosition.getCodeName()));

        if (passwordIsRequiredBefore && !passwordIsRequiredAfter)
            keycloakRoles.add(readRole(PASSWORD_REQUIRED_ROLE));
            
        deleteRolesAtUser(userId, keycloakRoles);

        if (passwordIsRequiredBefore && !passwordIsRequiredAfter)
            keycloakCredentialService.clearRequiredActions(userId);   // It's safer to delete roles earlier, then delete password

        keycloakRoles.clear();
        keycloakRoles.add(readRole(newPosition.getCodeName()));

        if (!passwordIsRequiredBefore && passwordIsRequiredAfter) 
            keycloakRoles.add(readRole(PASSWORD_REQUIRED_ROLE));

        assignRolesAtUser(userId, keycloakRoles);

        if (!passwordIsRequiredBefore && passwordIsRequiredAfter)
            keycloakCredentialService.setCredential(userId);

        keycloakRestClient.post()   // Logout to invalidate the JWT containing old Position
            .uri("/users/{userId}/logout", userId)
            .retrieve()             // No body sent for logout
            .toBodilessEntity();
    }

    public void deleteRolesAtUser(
        String userId, 
        List<KeycloakRole> roles
    ) {
        keycloakRestClient.method(HttpMethod.DELETE)    // delete() doesn't accept body()
            .uri("/users/{id}/role-mappings/realm", userId)
            .body(roles)
            .retrieve()
            .toBodilessEntity();
    }
}
