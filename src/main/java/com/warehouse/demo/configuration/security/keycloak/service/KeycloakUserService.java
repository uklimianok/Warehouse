package com.warehouse.demo.configuration.security.keycloak.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakCredential;
import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakRole;
import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakUser;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.util.info.Department;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class KeycloakUserService {
    private final RestClient keycloakRestClient;

    private final KeycloakRoleService keycloakRoleService;

    @Value("${warehouse.shared-password}")
    private String sharedPassword;

    public void create(Employee employee) {
        boolean passwordIsRequired = !employee.getPosition().getDepartment().equals(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT);

        KeycloakCredential keycloakCredential = new KeycloakCredential(
            "password",
            sharedPassword, 
            true
        );
        KeycloakUser keycloakUser = new KeycloakUser(
            employee.getEmployeeNumber(), 
            false, 
            employee.getFirstName(), 
            employee.getLastName(), 
            passwordIsRequired ? List.of(keycloakCredential) : null
        );

        URI location = createUser(keycloakUser);
        String userId = location.getPath().substring(location.getPath().lastIndexOf("/") + 1);

        List<KeycloakRole> keycloakRoles = new ArrayList<>();
        keycloakRoles.add(keycloakRoleService.readByName(employee.getPosition().getCodeName()));
        if (passwordIsRequired)
            keycloakRoles.add(keycloakRoleService.readByName("password_required"));

        assignRoles(userId, keycloakRoles);
        enableUser(userId);
    }

    private URI createUser(KeycloakUser user) {
        ResponseEntity<Void> response = keycloakRestClient.post()
            .uri("/users")
            .body(user)
            .retrieve()
            .toBodilessEntity();
        return response.getHeaders().getLocation();
    }

    private void assignRoles(String userId, List<KeycloakRole> roles) {
        keycloakRestClient.post()
            .uri("/users/{id}/role-mappings/realm", userId)
            .body(roles)
            .retrieve()
            .toBodilessEntity();
    }

    private void enableUser(String userId) {
        Map<String, Boolean> body = Map.of(
            "enabled", true
        );

        keycloakRestClient.put()
            .uri("/users/{userId}", userId)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }
}
