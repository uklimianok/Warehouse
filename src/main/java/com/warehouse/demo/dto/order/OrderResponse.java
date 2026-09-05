package com.warehouse.demo.dto.order;

import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;

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
