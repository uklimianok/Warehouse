package com.warehouse.demo.dto.employee;

import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.employee.position.PositionResponse;
import com.warehouse.demo.dto.employee.shift.ShiftResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DataControllerEmployeeResponse extends EmployeeResponse {
    private ShiftResponse shift;

    public DataControllerEmployeeResponse(long id, String firstName, String lastName, OrganizationResponse employerOrganization, String employeeNumber, PositionResponse position, ShiftResponse shift) {
        super(id, firstName, lastName, employerOrganization, employeeNumber, position);
        this.shift = shift;
    }
}
