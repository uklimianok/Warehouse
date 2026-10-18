package local.warehouse.app.mapper.workplace.gate;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.workplace.gate.GateRequest;
import local.warehouse.app.entity.workplace.Gate;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface GateRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(GateRequest gateRequest, @MappingTarget Gate gate);
}
