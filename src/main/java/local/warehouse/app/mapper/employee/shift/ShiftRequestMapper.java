package local.warehouse.app.mapper.employee.shift;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.employee.shift.ShiftRequest;
import local.warehouse.app.entity.employee.Shift;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface ShiftRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(ShiftRequest shiftRequest, @MappingTarget Shift shift);
}
