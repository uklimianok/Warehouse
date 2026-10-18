package local.warehouse.app.service.employee;

import local.warehouse.app.dto.employee.organizationType.OrganizationTypeRequest;
import local.warehouse.app.entity.employee.OrganizationType;
import local.warehouse.app.service.BaseService;

public interface OrganizationTypeService extends BaseService<OrganizationType, Long> {
    OrganizationType create(OrganizationTypeRequest organizationTypeRequest);
    OrganizationType update(long id, OrganizationTypeRequest organizationTypeRequest);
}
