package com.warehouse.demo.service.employee;

import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.service.BaseService;

public interface OrganizationTypeService extends BaseService<OrganizationType, Long> {
    OrganizationType create(OrganizationTypeRequest organizationTypeRequest);
    OrganizationType update(long id, OrganizationTypeRequest organizationTypeRequest);
}
