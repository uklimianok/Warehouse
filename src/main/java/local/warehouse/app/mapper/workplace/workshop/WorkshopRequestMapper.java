package local.warehouse.app.mapper.workplace.workshop;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.workplace.workshop.WorkshopRequest;
import local.warehouse.app.entity.workplace.Workshop;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface WorkshopRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(WorkshopRequest workshopRequest, @MappingTarget Workshop workshop);
}
