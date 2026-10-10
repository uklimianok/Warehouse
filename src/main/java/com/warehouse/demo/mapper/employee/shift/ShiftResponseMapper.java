package com.warehouse.demo.mapper.employee.shift;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.employee.shift.ShiftResponse;
import com.warehouse.demo.entity.employee.Shift;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ShiftResponseMapper {
    ShiftResponse convertToResponse(Shift shift);
}
