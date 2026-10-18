package local.warehouse.app.mapper.workplace.workshop;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.workplace.workshop.WorkshopResponse;
import local.warehouse.app.entity.workplace.Workshop;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface WorkshopResponseMapper {
    WorkshopResponse convertToResponse(Workshop workshop);
}
