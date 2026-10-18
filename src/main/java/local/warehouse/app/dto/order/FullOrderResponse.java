package local.warehouse.app.dto.order;

import local.warehouse.app.dto.employee.organization.OrganizationResponse;
import local.warehouse.app.dto.employee.shift.ShiftResponse;
import local.warehouse.app.dto.service.status.StatusResponse;
import local.warehouse.app.dto.workplace.gate.GateResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullOrderResponse extends OrderResponse {
    private ShiftResponse shift;
    private StatusResponse status;

    public FullOrderResponse(
        long id, 
        OrganizationResponse store, 
        GateResponse gate, 
        ShiftResponse shift, 
        StatusResponse status, 
        String note
    ) {
        super(
            id, 
            store, 
            gate, 
            note
        );
        this.shift = shift;
        this.status = status;
    }
}
