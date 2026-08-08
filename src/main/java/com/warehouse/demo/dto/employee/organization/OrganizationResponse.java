package com.warehouse.demo.dto.employee.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrganizationResponse {
    private long id;
    private String name;
    private String organizationNumber;
}
