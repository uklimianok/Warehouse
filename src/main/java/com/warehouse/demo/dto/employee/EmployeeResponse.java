package com.warehouse.demo.dto.employee;

import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.employee.position.PositionResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmployeeResponse {
    private long id;
    private String firstName;
    private String lastName;
    private OrganizationResponse organization;
    private String employeeNumber;
    private PositionResponse position;
}
