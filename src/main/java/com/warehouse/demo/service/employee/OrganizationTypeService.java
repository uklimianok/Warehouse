package com.warehouse.demo.service.employee;

import java.util.List;

import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.entity.employee.OrganizationType;

public interface OrganizationTypeService {
    List<OrganizationType> readAll();
    OrganizationType readById(long id);
    OrganizationType create(OrganizationTypeRequest organizationTypeRequest);
    OrganizationType update(long id, OrganizationTypeRequest organizationTypeRequest);
    void deleteById(long id);
}
