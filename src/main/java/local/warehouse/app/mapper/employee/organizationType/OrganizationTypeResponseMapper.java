package local.warehouse.app.mapper.employee.organizationType;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.employee.organizationType.OrganizationTypeResponse;
import local.warehouse.app.entity.employee.OrganizationType;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface OrganizationTypeResponseMapper {
    OrganizationTypeResponse convertToResponse(OrganizationType organizationType);
}
