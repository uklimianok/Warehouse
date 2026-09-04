package com.warehouse.demo.mapper.employee;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.employee.DataControllerEmployeeResponse;
import com.warehouse.demo.dto.employee.EmployeeResponse;
import com.warehouse.demo.dto.employee.FullEmployeeResponse;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.mapper.employee.organization.OrganizationResponseMapper;
import com.warehouse.demo.mapper.employee.position.PositionResponseMapper;
import com.warehouse.demo.mapper.employee.shift.ShiftResponseMapper;

@Mapper(componentModel = "spring", uses = {OrganizationResponseMapper.class, PositionResponseMapper.class, ShiftResponseMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EmployeeResponseMapper {
    EmployeeResponse convertToResponse(Employee employee);
    DataControllerEmployeeResponse convertToDataControllerResponse(Employee employee);
    FullEmployeeResponse convertToFullResponse(Employee employee);
}
