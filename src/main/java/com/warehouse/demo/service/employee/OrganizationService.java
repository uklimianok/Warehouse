package com.warehouse.demo.service.employee;

import com.warehouse.demo.dto.employee.organization.OrganizationRequest;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.service.AbstractService;

public interface OrganizationService extends AbstractService<Organization, Long> {
    Organization create(OrganizationRequest organizationRequest);
    Organization update(long id, OrganizationRequest organizationRequest);
}
