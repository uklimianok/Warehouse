package com.warehouse.demo.mapper.service.status;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.service.status.StatusRequest;
import com.warehouse.demo.entity.service.Status;

@Mapper(componentModel = "spring")
public interface StatusRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(StatusRequest statusRequest, @MappingTarget Status status);
}
