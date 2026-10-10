package com.warehouse.demo.mapper.employee.organizationType;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.entity.employee.OrganizationType;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrganizationTypeResponseMapper {
    OrganizationTypeResponse convertToResponse(OrganizationType organizationType);
}
