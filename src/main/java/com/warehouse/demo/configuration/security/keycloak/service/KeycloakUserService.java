package com.warehouse.demo.configuration.security.keycloak.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakCredential;
import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakRole;
import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakUserRequest;
import com.warehouse.demo.configuration.security.keycloak.dto.KeycloakUserResponse;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.util.info.Department;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class KeycloakUserService {
    private final RestClient keycloakRestClient;

    private final String PASSWORD_REQUIRED_ROLE = "password_required";

    @Value("${warehouse.shared-password}")
    private String sharedPassword;

    public Optional<String> readIdByUsername(String employeeNumber) {
        ResponseEntity<KeycloakUserResponse[]> response = keycloakRestClient.get()
            .uri("/users?username={username}&exact=true", employeeNumber)
            .retrieve()
            .toEntity(KeycloakUserResponse[].class);
        
        if (response.getBody().length == 0)
            return Optional.empty();

        KeycloakUserResponse keycloakUser = response.getBody()[0];

        return Optional.of(keycloakUser.id());
    }

    public void create(Employee employee) {
        boolean passwordIsRequired = !employee.getPosition().getDepartment().equals(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT);

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

        URI location = createUser(keycloakUser);
        String userId = location.getPath().substring(location.getPath().lastIndexOf("/") + 1);

        List<KeycloakRole> keycloakRoles = new ArrayList<>();
        keycloakRoles.add(readRole(employee.getPosition().getCodeName()));
        if (passwordIsRequired)
            keycloakRoles.add(readRole(PASSWORD_REQUIRED_ROLE));

        assignRoles(userId, keycloakRoles);
        setEnabled(userId, true);
    }

    public void delete(String employeeNumber) {
        readIdByUsername(employeeNumber).ifPresent(id -> 
            keycloakRestClient.delete()
                .uri("/users/{userId}", id)
                .retrieve()
                .toBodilessEntity()
        );
    }

    public void updatePosition(Position oldPosition, Employee employee) {
        Optional<String> userId = readIdByUsername(employee.getEmployeeNumber());
        if (userId.isEmpty()) {
            if (employee.getPosition().isHasDatabaseAccess())
                create(employee);

            return;
        }

        updateRoles(userId.get(), oldPosition, employee.getPosition());

        boolean DBAccessIsChanged = oldPosition.isHasDatabaseAccess() != employee.getPosition().isHasDatabaseAccess();
        if (DBAccessIsChanged)
            setEnabled(userId.get(), employee.getPosition().isHasDatabaseAccess());
    }

    private URI createUser(KeycloakUserRequest userRequest) {
        ResponseEntity<Void> response = keycloakRestClient.post()
            .uri("/users")
            .body(userRequest)
            .retrieve()
            .toBodilessEntity();
        return response.getHeaders().getLocation();
    }

    public KeycloakRole readRole(String roleName) {
        ResponseEntity<KeycloakRole> response = keycloakRestClient.get()
            .uri("/roles/{roleName}", roleName)
            .retrieve()
            .toEntity(KeycloakRole.class);

        return response.getBody();
    }

    private void assignRoles(String userId, List<KeycloakRole> roles) {
        keycloakRestClient.post()
            .uri("/users/{id}/role-mappings/realm", userId)
            .body(roles)
            .retrieve()
            .toBodilessEntity();
    }

    private void deleteRoles(String userId, List<KeycloakRole> roles) {
        keycloakRestClient.method(HttpMethod.DELETE)    // delete() doesn't accept body()
            .uri("/users/{id}/role-mappings/realm", userId)
            .body(roles)
            .retrieve()
            .toBodilessEntity();
    }

    private void updateRoles(String userId, Position oldPosition, Position newPosition) {
        boolean passwordIsRequiredBefore = !oldPosition.getDepartment().equals(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT);
        boolean passwordIsRequiredAfter = !newPosition.getDepartment().equals(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT);

        List<KeycloakRole> keycloakRoles = new ArrayList<>();
        keycloakRoles.add(readRole(oldPosition.getCodeName()));

        if (passwordIsRequiredBefore && !passwordIsRequiredAfter)
            keycloakRoles.add(readRole(PASSWORD_REQUIRED_ROLE));
            
        deleteRoles(userId, keycloakRoles);

        if (passwordIsRequiredBefore && !passwordIsRequiredAfter)
            clearRequiredActions(userId);   // It's safer to delete roles, then delete password

        keycloakRoles.clear();
        keycloakRoles.add(readRole(newPosition.getCodeName()));

        if (!passwordIsRequiredBefore && passwordIsRequiredAfter) 
            keycloakRoles.add(readRole(PASSWORD_REQUIRED_ROLE));

        assignRoles(userId, keycloakRoles);

        if (!passwordIsRequiredBefore && passwordIsRequiredAfter)
            setCredential(userId);
    }

    private void setEnabled(String userId, boolean enabled) {
        Map<String, Boolean> body = Map.of(
            "enabled", enabled
        );

        keycloakRestClient.put()
            .uri("/users/{userId}", userId)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }

    private void setCredential(String userId) {
        KeycloakCredential keycloakCredential = new KeycloakCredential(
            "password", 
            sharedPassword, 
            true
        );

        keycloakRestClient.put()
            .uri("/users/{userId}/reset-password", userId)
            .body(keycloakCredential)
            .retrieve()
            .toBodilessEntity();
    }

    private void clearRequiredActions(String userId) {
        Map<String, List<String>> body = Map.of(
            "requiredActions", List.of()
        );

        keycloakRestClient.put()
            .uri("/users/{userId}", userId)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }
}
