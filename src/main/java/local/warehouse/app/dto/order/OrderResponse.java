package local.warehouse.app.dto.order;

import local.warehouse.app.dto.employee.organization.OrganizationResponse;
import local.warehouse.app.dto.workplace.gate.GateResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class OrderResponse {
    private long id;
    private OrganizationResponse store;
    private GateResponse gate;
    private String note;
}
