package local.warehouse.app.mapper.employee.position;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.employee.position.FullPositionResponse;
import local.warehouse.app.dto.employee.position.PositionResponse;
import local.warehouse.app.entity.employee.Position;
import local.warehouse.app.mapper.employee.department.DepartmentResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = DepartmentResponseMapper.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PositionResponseMapper {
    PositionResponse convertToResponse(Position position);
    FullPositionResponse convertToFullResponse(Position position);
}
