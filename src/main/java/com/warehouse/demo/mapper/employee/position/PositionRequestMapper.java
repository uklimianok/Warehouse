package com.warehouse.demo.mapper.employee.position;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.entity.employee.Position;

@Mapper(componentModel = "spring")
public interface PositionRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codeName", ignore = true)
    void convertFromRequest(PositionRequest positionRequest, @MappingTarget Position position);
}
