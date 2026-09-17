package com.warehouse.demo.dto.employee;

import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.employee.position.PositionResponse;
import com.warehouse.demo.dto.employee.shift.ShiftResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.dto.workplace.workshop.WorkshopResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class DataControllerEmployeeResponse extends EmployeeResponse {
    private ShiftResponse shift;

    public DataControllerEmployeeResponse(long id, String firstName, String lastName, OrganizationResponse employerOrganization, String employeeNumber, PositionResponse position, ShiftResponse shift, WorkshopResponse workshop, GateResponse gate) {
        super(id, firstName, lastName, employerOrganization, employeeNumber, position, workshop, gate);
        this.shift = shift;
    }
}
