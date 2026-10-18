package local.warehouse.app.dto.employee;

import java.time.LocalDate;

import local.warehouse.app.dto.employee.organization.OrganizationResponse;
import local.warehouse.app.dto.employee.position.PositionResponse;
import local.warehouse.app.dto.employee.shift.ShiftResponse;
import local.warehouse.app.dto.workplace.gate.GateResponse;
import local.warehouse.app.dto.workplace.workshop.WorkshopResponse;
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

    public FullEmployeeResponse(
        long id, 
        String firstName, 
        String lastName, 
        OrganizationResponse employerOrganization, 
        String employeeNumber, 
        PositionResponse position, 
        ShiftResponse shift, 
        LocalDate birthDate, 
        String documentId, 
        String residenceAddress, 
        String phoneNumber, 
        WorkshopResponse workshop, 
        GateResponse gate
    ) {
        super(
            id, 
            firstName, 
            lastName, 
            employerOrganization, 
            employeeNumber, 
            position, 
            shift, 
            workshop, 
            gate
        );
        this.birthDate = birthDate;
        this.documentId = documentId;
        this.residenceAddress = residenceAddress;
        this.phoneNumber = phoneNumber;
    }
}
