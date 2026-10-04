package com.warehouse.demo.configuration.security.keycloak.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakCredential;
import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakRole;
import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakUserRequest;
import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakUserResponse;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.util.info.DepartmentCodeNames;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class KeycloakUserService {
    private final RestClient keycloakRestClient;

    private final KeycloakRoleService keycloakRoleService;

    private static final String PASSWORD_REQUIRED_ROLE = "password_required";

    @Value("${warehouse.shared-password}")
    private String sharedPassword;

    public Optional<String> readIdByUsername(
        String employeeNumber
    ) {
        ResponseEntity<KeycloakUserResponse[]> response = keycloakRestClient.get()
            .uri("/users?username={username}&exact=true", employeeNumber)
            .retrieve()
            .toEntity(KeycloakUserResponse[].class);
        
        if (response.getBody().length == 0)
            return Optional.empty();

        KeycloakUserResponse keycloakUser = response.getBody()[0];

        return Optional.of(keycloakUser.id());
    }

    public void createUser(
        Employee employee
    ) {
        boolean passwordIsRequired = !employee.getPosition().getDepartment().getCodeName().equals(DepartmentCodeNames.WAREHOUSE_EMPLOYEES_DEPARTMENT);

        KeycloakCredential keycloakCredential = new KeycloakCredential(
            "password",
            sharedPassword, 
            true
        );
        KeycloakUserRequest keycloakUser = new KeycloakUserRequest(
            employee.getEmployeeNumber(), 
            false, 
            employee.getFirstName(), 
            employee.getLastName(), 
            passwordIsRequired ? List.of(keycloakCredential) : null
        );

        URI location = keycloakRestClient.post()
            .uri("/users")
            .body(keycloakUser)
            .retrieve()
            .toBodilessEntity()
            .getHeaders()
            .getLocation();
        String userId = location.getPath().substring(location.getPath().lastIndexOf("/") + 1);

        List<KeycloakRole> keycloakRoles = new ArrayList<>();
        keycloakRoles.add(keycloakRoleService.readRole(employee.getPosition().getCodeName()));
        if (passwordIsRequired)
            keycloakRoles.add(keycloakRoleService.readRole(PASSWORD_REQUIRED_ROLE));

        keycloakRoleService.assignRolesAtUser(userId, keycloakRoles);
        setEnabled(userId, true);
    }

    public void deleteUser(
        String employeeNumber
    ) {
        readIdByUsername(employeeNumber).ifPresent(id -> 
            keycloakRestClient.delete()
                .uri("/users/{userId}", id)
                .retrieve()
                .toBodilessEntity()
        );
    }

    public void updatePositionAtUser(
        Position oldPosition, 
        Employee employee
    ) {
        Optional<String> userId = readIdByUsername(employee.getEmployeeNumber());
        if (userId.isEmpty()) {
            if (employee.getPosition().isEnabled())
                createUser(employee);

            return;
        }

        keycloakRoleService.updateRolesAtUser(userId.get(), oldPosition, employee.getPosition());

        boolean DBAccessIsChanged = oldPosition.isEnabled() != employee.getPosition().isEnabled();
        if (DBAccessIsChanged)
            setEnabled(userId.get(), employee.getPosition().isEnabled());
    }

    private void setEnabled(
        String userId, 
        boolean enabled
    ) {
        Map<String, Boolean> body = Map.of(
            "enabled", enabled
        );

        keycloakRestClient.put()
            .uri("/users/{userId}", userId)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }
}
