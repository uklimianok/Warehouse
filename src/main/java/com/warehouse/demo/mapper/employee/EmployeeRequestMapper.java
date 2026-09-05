package com.warehouse.demo.mapper.employee;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.mapper.employee.organization.OrganizationResolver;
import com.warehouse.demo.mapper.employee.position.PositionResolver;
import com.warehouse.demo.mapper.employee.shift.ShiftResolver;

@Mapper(componentModel = "spring", uses = {OrganizationResolver.class, PositionResolver.class, ShiftResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EmployeeRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeNumber", ignore = true)
    @Mapping(target = "employerOrganization", source = "employeeRequest.employerOrganizationId")
    @Mapping(target = "position", source = "employeeRequest.positionId")
    @Mapping(target = "shift", source = "employeeRequest.shiftId")
    void convertFromRequest(EmployeeRequest employeeRequest, @MappingTarget Employee employee);
}
