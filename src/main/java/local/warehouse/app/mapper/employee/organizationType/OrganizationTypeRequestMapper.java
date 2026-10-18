package local.warehouse.app.mapper.employee.organizationType;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.employee.organizationType.OrganizationTypeRequest;
import local.warehouse.app.entity.employee.OrganizationType;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface OrganizationTypeRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(OrganizationTypeRequest organizationTypeRequest, @MappingTarget OrganizationType organizationType);
}
