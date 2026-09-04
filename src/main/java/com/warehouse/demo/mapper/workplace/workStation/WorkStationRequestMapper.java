package com.warehouse.demo.mapper.workplace.workStation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.workplace.workStation.WorkStationRequest;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.mapper.workplace.workshop.WorkshopResolver;

@Mapper(componentModel = "spring", uses = WorkshopResolver.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WorkStationRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workshop", source = "workStationRequest.workshopId")
    WorkStation convertFromRequest(WorkStationRequest workStationRequest, @MappingTarget WorkStation workStation);
}
