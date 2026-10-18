package local.warehouse.app.dto.employee;

import local.warehouse.app.dto.employee.organization.OrganizationResponse;
import local.warehouse.app.dto.employee.position.PositionResponse;
import local.warehouse.app.dto.workplace.gate.GateResponse;
import local.warehouse.app.dto.workplace.workshop.WorkshopResponse;
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
