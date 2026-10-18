package local.warehouse.app.mapper.employee.shift;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.employee.shift.ShiftResponse;
import local.warehouse.app.entity.employee.Shift;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface ShiftResponseMapper {
    ShiftResponse convertToResponse(Shift shift);
}
