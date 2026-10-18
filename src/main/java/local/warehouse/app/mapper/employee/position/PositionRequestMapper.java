package local.warehouse.app.mapper.employee.position;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.employee.position.PositionRequest;
import local.warehouse.app.entity.employee.Position;
import local.warehouse.app.mapper.employee.department.DepartmentResolver;

@Mapper(
    componentModel = "spring", 
    uses = {DepartmentResolver.class, PositionResolver.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface PositionRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codeName", ignore = true)
    @Mapping(target = "department", source = "positionRequest.departmentId")
    @Mapping(target = "inheritedPositions", source = "inheritedPositionsId")
    @Mapping(target = "removable", ignore = true)
    void convertFromRequest(PositionRequest positionRequest, @MappingTarget Position position);
}
