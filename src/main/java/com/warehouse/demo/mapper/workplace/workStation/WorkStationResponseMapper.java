package com.warehouse.demo.mapper.workplace.workStation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.workplace.workStation.FullWorkStationResponse;
import com.warehouse.demo.dto.workplace.workStation.OperatorWorkStationResponse;
import com.warehouse.demo.dto.workplace.workStation.WorkStationResponse;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.mapper.workplace.workshop.WorkshopResponseMapper;

@Mapper(componentModel = "spring", uses = WorkshopResponseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WorkStationResponseMapper {
    WorkStationResponse convertToResponse(WorkStation workStation);
    OperatorWorkStationResponse convertToOperatorResponse(WorkStation workStation);
    FullWorkStationResponse convertToFullResponse(WorkStation workStation);
}
