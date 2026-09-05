package com.warehouse.demo.mapper.employee.shift;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.employee.shift.ShiftRequest;
import com.warehouse.demo.entity.employee.Shift;

@Mapper(componentModel = "spring")
public interface ShiftRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(ShiftRequest shiftRequest, @MappingTarget Shift shift);
}
