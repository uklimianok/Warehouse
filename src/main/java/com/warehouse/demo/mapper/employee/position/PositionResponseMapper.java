package com.warehouse.demo.mapper.employee.position;

import org.mapstruct.Mapper;

import com.warehouse.demo.dto.employee.position.FullPositionResponse;
import com.warehouse.demo.dto.employee.position.PositionResponse;
import com.warehouse.demo.entity.employee.Position;

@Mapper(componentModel = "spring")
public interface PositionResponseMapper {
    PositionResponse convertToResponse(Position position);
    FullPositionResponse convertToFullResponse(Position position);
}
