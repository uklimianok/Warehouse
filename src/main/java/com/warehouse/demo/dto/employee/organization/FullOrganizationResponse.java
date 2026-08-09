package com.warehouse.demo.dto.employee.organization;

import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FullOrganizationResponse extends OrganizationResponse {
    private OrganizationTypeResponse organizationType;
    private String address;
    private String phoneNumber;
    private String email;
    private String url;

    public FullOrganizationResponse(long id, String name, String organizationNumber, OrganizationTypeResponse organizationType, String address, String phoneNumber, String email, String url) {
        super(id, name, organizationNumber);
        this.organizationType = organizationType;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.url = url;
    }
}
