package com.warehouse.demo.mapper.workplace.workshop;

import org.mapstruct.Mapper;

import com.warehouse.demo.dto.workplace.workshop.WorkshopResponse;
import com.warehouse.demo.entity.workplace.Workshop;

@Mapper(componentModel = "spring")
public interface WorkshopResponseMapper {
    WorkshopResponse convertToResponse(Workshop workshop);
}
