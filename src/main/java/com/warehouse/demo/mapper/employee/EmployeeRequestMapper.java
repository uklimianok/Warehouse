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
    @Mapping(target = "workshop", ignore = true)
    @Mapping(target = "gate", ignore = true)
    void convertFromRequest(EmployeeRequest employeeRequest, @MappingTarget Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "employeeNumber", ignore = true)
    @Mapping(target = "employerOrganization", ignore = true)
    @Mapping(target = "position", source = "employeeRequest.positionId")
    @Mapping(target = "shift", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "documentId", ignore = true)
    @Mapping(target = "residenceAddress", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "workshop", ignore = true)
    @Mapping(target = "gate", ignore = true)
    void convertFromWarehouseEmployeeDepartmentRequest(EmployeeRequest employeeRequest, @MappingTarget Employee employee);
}
