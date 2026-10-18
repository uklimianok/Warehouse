package local.warehouse.app.mapper.service.status;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.service.status.StatusResponse;
import local.warehouse.app.entity.service.Status;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface StatusResponseMapper {
    StatusResponse convertToResponse(Status status);
}
