package local.warehouse.app.mapper.employee.organization;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.employee.organization.FullOrganizationResponse;
import local.warehouse.app.dto.employee.organization.OrganizationResponse;
import local.warehouse.app.entity.employee.Organization;
import local.warehouse.app.mapper.employee.organizationType.OrganizationTypeResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = OrganizationTypeResponseMapper.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface OrganizationResponseMapper {
    OrganizationResponse convertToResponse(Organization organization);
    FullOrganizationResponse convertToFullResponse(Organization organization);
}
