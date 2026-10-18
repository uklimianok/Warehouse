package local.warehouse.app.service.employee;

import local.warehouse.app.dto.employee.organization.OrganizationRequest;
import local.warehouse.app.entity.employee.Organization;
import local.warehouse.app.service.BaseService;

public interface OrganizationService extends BaseService<Organization, Long> {
    Organization create(OrganizationRequest organizationRequest);
    Organization update(long id, OrganizationRequest organizationRequest);
}
