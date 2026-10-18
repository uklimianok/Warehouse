package local.warehouse.app.mapper.employee.organization;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.employee.organization.OrganizationRequest;
import local.warehouse.app.entity.employee.Organization;
import local.warehouse.app.mapper.employee.organizationType.OrganizationTypeResolver;

@Mapper(
    componentModel = "spring", 
    uses = OrganizationTypeResolver.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface OrganizationRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizationType", source = "organizationRequest.organizationTypeId")
    void convertFromRequest(OrganizationRequest organizationRequest, @MappingTarget Organization organization);
}
