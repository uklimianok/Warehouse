package com.warehouse.demo.dto.product;

import java.math.BigDecimal;

import com.warehouse.demo.dto.employee.organization.FullOrganizationResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FullProductResponse extends ProductResponse {
    private String barcodeNumber;
    private BigDecimal cost;
    private FullOrganizationResponse producer;

    public FullProductResponse(long id, String name, String barcodeNumber, BigDecimal cost, FullOrganizationResponse producer) {
        super(id, name);
        this.barcodeNumber = barcodeNumber;
        this.cost = cost;
        this.producer = producer;
    }
}
