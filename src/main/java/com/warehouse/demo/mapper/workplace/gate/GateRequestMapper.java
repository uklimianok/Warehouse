package com.warehouse.demo.mapper.workplace.gate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.workplace.gate.GateRequest;
import com.warehouse.demo.entity.workplace.Gate;

@Mapper(componentModel = "spring")
public interface GateRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(GateRequest gateRequest, @MappingTarget Gate gate);
}
