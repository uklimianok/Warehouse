package com.warehouse.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.warehouse.demo.configuration.security.keycloak.service.KeycloakUserService;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.util.info.Department;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class OAuth2Runner implements CommandLineRunner {
    private final KeycloakUserService keycloakUserService;

    @Value("${warehouse.shared-password}")
    private String sharedPassword;

    @Override
    public void run(String... args) throws Exception {
        Employee employee1 = createEmployeeWithPassword();
        keycloakUserService.create(employee1);

        Employee employee2 = createEmployeeWithoutPassword();
        keycloakUserService.create(employee2);
    }

    private Employee createEmployeeWithPassword() {
        Position position = new Position();
        position.setCodeName("DATA_CONTROLLER");
        position.setDepartment(Department.AUXILIARY_EMPLOYEES_DEPARTMENT);

        Employee employee = new Employee();
        employee.setEmployeeNumber("99000001");
        employee.setFirstName("FirstName");
        employee.setLastName("LastName");
        employee.setPosition(position);

        return employee;
    }

    private Employee createEmployeeWithoutPassword() {
        Position position = new Position();
        position.setCodeName("GOODS_PICKER");
        position.setDepartment(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT);

        Employee employee = new Employee();
        employee.setEmployeeNumber("99000002");
        employee.setFirstName("FirstName");
        employee.setLastName("LastName");
        employee.setPosition(position);

        return employee;
    }
}
