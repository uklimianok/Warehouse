package com.warehouse.demo.mapper.employee.position;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.mapper.employee.department.DepartmentResolver;

@Mapper(componentModel = "spring", uses = {DepartmentResolver.class, PositionResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PositionRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codeName", ignore = true)
    @Mapping(target = "department", source = "positionRequest.departmentId")
    @Mapping(target = "inheritedPositions", source = "inheritedPositionsId")
    void convertFromRequest(PositionRequest positionRequest, @MappingTarget Position position);
}
