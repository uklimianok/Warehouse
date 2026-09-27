package com.warehouse.demo.mapper.employee.position;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.employee.position.FullPositionResponse;
import com.warehouse.demo.dto.employee.position.PositionResponse;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.mapper.employee.department.DepartmentResponseMapper;

@Mapper(componentModel = "spring", uses = DepartmentResponseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PositionResponseMapper {
    PositionResponse convertToResponse(Position position);
    FullPositionResponse convertToFullResponse(Position position);
}
