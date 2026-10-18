package local.warehouse.app.mapper.workplace.gate;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.workplace.gate.GateResponse;
import local.warehouse.app.entity.workplace.Gate;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface GateResponseMapper {
    GateResponse convertToResponse(Gate gate);
}
