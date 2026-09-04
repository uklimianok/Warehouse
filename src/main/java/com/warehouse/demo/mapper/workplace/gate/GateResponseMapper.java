package com.warehouse.demo.mapper.workplace.gate;

import org.mapstruct.Mapper;

import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.workplace.Gate;

@Mapper(componentModel = "spring")
public interface GateResponseMapper {
    GateResponse convertToResponse(Gate gate);
}
