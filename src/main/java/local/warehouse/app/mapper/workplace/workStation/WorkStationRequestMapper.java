package local.warehouse.app.mapper.workplace.workStation;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.workplace.workStation.WorkStationRequest;
import local.warehouse.app.entity.workplace.WorkStation;
import local.warehouse.app.mapper.workplace.workshop.WorkshopResolver;

@Mapper(
    componentModel = "spring", 
    uses = WorkshopResolver.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface WorkStationRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workshop", source = "workStationRequest.workshopId")
    void convertFromRequest(WorkStationRequest workStationRequest, @MappingTarget WorkStation workStation);
}
