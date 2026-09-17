package com.warehouse.demo.dto.employee;

import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.employee.position.PositionResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.dto.workplace.workshop.WorkshopResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class EmployeeResponse {
    private long id;
    private String firstName;
    private String lastName;
    private OrganizationResponse employerOrganization;
    private String employeeNumber;
    private PositionResponse position;
    private WorkshopResponse workshop;
    private GateResponse gate;
}
