package com.warehouse.demo.dto.employee;

import java.time.LocalDate;

import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.employee.position.PositionResponse;
import com.warehouse.demo.dto.employee.shift.ShiftResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullEmployeeResponse extends DataControllerEmployeeResponse {
    private LocalDate birthDate;
    private String documentId;
    private String residenceAddress;
    private String phoneNumber;

    public FullEmployeeResponse(long id, String firstName, String lastName, OrganizationResponse employerOrganization, String employeeNumber, PositionResponse position, ShiftResponse shift, LocalDate birthDate, String documentId, String residenceAddress, String phoneNumber) {
        super(id, firstName, lastName, employerOrganization, employeeNumber, position, shift);
        this.birthDate = birthDate;
        this.documentId = documentId;
        this.residenceAddress = residenceAddress;
        this.phoneNumber = phoneNumber;
    }
}
