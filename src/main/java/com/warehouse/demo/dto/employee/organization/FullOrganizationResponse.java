package com.warehouse.demo.dto.employee.organization;

import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FullOrganizationResponse extends OrganizationResponse {
    private OrganizationTypeResponse organizationType;
    private String address;
    private String phone;
    private String email;
    private String url;

    public FullOrganizationResponse(long id, String name, String organizationNumber, OrganizationTypeResponse organizationType, String address, String phone, String email, String url) {
        super(id, name, organizationNumber);
        this.organizationType = organizationType;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.url = url;
    }
}
