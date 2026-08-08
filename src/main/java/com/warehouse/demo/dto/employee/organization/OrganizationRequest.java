package com.warehouse.demo.dto.employee.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationRequest {
    private String name;
    private String organizationNumber;
    private long organizationTypeId;
    private String address;
    private String phone;
    private String email;
    private String url;
}
