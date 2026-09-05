package com.warehouse.demo.mapper.employee.organizationType;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.entity.employee.OrganizationType;

@Mapper(componentModel = "spring")
public interface OrganizationTypeRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(OrganizationTypeRequest organizationTypeRequest, @MappingTarget OrganizationType organizationType);
}
