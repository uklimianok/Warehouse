package com.warehouse.demo.mapper.workplace.workshop;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.workplace.workshop.WorkshopRequest;
import com.warehouse.demo.entity.workplace.Workshop;

@Mapper(componentModel = "spring")
public interface WorkshopRequestMapper {
    @Mapping(target = "id", ignore = true)
    Workshop convertFromRequest(WorkshopRequest workshopRequest, @MappingTarget Workshop workshop);
}
