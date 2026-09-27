package com.warehouse.demo.mapper.employee.department;

import org.mapstruct.Mapper;

import com.warehouse.demo.dto.employee.department.DepartmentResponse;
import com.warehouse.demo.dto.employee.department.FullDepartmentResponse;
import com.warehouse.demo.entity.employee.Department;

@Mapper(componentModel = "spring")
public interface DepartmentResponseMapper {
    DepartmentResponse convertToResponse(Department department);
    FullDepartmentResponse convertToFullResponse(Department department);
}
