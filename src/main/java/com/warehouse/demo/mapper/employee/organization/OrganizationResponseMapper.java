package com.warehouse.demo.mapper.employee.organization;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.employee.organization.FullOrganizationResponse;
import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.mapper.employee.organizationType.OrganizationTypeResponseMapper;

@Mapper(componentModel = "spring", uses = OrganizationTypeResponseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrganizationResponseMapper {
    OrganizationResponse convertToResponse(Organization organization);
    FullOrganizationResponse convertToFullResponse(Organization organization);
}
