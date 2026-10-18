package local.warehouse.app.mapper.workplace.workStation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.workplace.workStation.FullWorkStationResponse;
import local.warehouse.app.dto.workplace.workStation.WorkStationResponse;
import local.warehouse.app.entity.workplace.WorkStation;
import local.warehouse.app.mapper.workplace.workshop.WorkshopResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = WorkshopResponseMapper.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface WorkStationResponseMapper {
    WorkStationResponse convertToResponse(WorkStation workStation);
    FullWorkStationResponse convertToFullResponse(WorkStation workStation);
}
