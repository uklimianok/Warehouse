package com.warehouse.demo.dto.order;

import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.employee.shift.ShiftResponse;
import com.warehouse.demo.dto.service.status.StatusResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullOrderResponse extends OrderResponse {
    private ShiftResponse shift;
    private StatusResponse status;

    public FullOrderResponse(long id, OrganizationResponse store, GateResponse gate, ShiftResponse shift, StatusResponse status, String note) {
        super(id, store, gate, note);
        this.shift = shift;
        this.status = status;
    }
}
