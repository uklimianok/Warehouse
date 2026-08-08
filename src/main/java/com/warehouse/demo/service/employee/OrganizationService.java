package com.warehouse.demo.service.employee;

import java.util.List;

import com.warehouse.demo.dto.employee.organization.OrganizationRequest;
import com.warehouse.demo.entity.employee.Organization;

public interface OrganizationService {
    List<Organization> readAll();
    Organization read(long id);
    Organization create(OrganizationRequest organizationRequest);
    Organization update(long id, OrganizationRequest organizationRequest);
    void delete(long id);
}
