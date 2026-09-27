package com.warehouse.demo.mapper.employee.department;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.employee.department.DepartmentRequest;
import com.warehouse.demo.entity.employee.Department;

@Mapper(componentModel = "spring")
public interface DepartmentRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codeName", ignore = true)
    void convertFromRequest(DepartmentRequest departmentRequest, @MappingTarget Department department);
}
