package local.warehouse.app.dto.employee;

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
public class DataControllerEmployeeResponse extends EmployeeResponse {
    private ShiftResponse shift;

    public DataControllerEmployeeResponse(
        long id, 
        String firstName, 
        String lastName, 
        OrganizationResponse employerOrganization, 
        String employeeNumber, 
        PositionResponse position, 
        ShiftResponse shift, 
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
            workshop, 
            gate
        );
        this.shift = shift;
    }
}
