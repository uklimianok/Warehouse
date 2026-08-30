package com.warehouse.demo.mapper.employee.organizationType;

import org.mapstruct.Mapper;

import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.entity.employee.OrganizationType;

@Mapper(componentModel = "spring")
public interface OrganizationTypeResponseMapper {
    OrganizationTypeResponse toResponse(OrganizationType organizationType);
}
