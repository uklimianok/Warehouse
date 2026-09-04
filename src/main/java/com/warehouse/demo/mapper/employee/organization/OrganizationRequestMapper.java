package com.warehouse.demo.mapper.employee.organization;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.employee.organization.OrganizationRequest;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.mapper.employee.organizationType.OrganizationTypeResolver;

@Mapper(componentModel = "spring", uses = OrganizationTypeResolver.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrganizationRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizationType", source = "organizationRequest.organizationTypeId")
    Organization convertFromRequest(OrganizationRequest organizationRequest, @MappingTarget Organization organization);
}
