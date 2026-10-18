package local.warehouse.app.mapper.service.actionLog;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.service.actionLog.ActionLogResponse;
import local.warehouse.app.entity.service.ActionLog;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface ActionLogResponseMapper {
    ActionLogResponse convertToResponse(ActionLog actionLog);
}
