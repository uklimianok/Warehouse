package com.warehouse.demo.mapper.employee.department;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.employee.department.DepartmentRequest;
import com.warehouse.demo.entity.employee.Department;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DepartmentRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codeName", ignore = true)
    @Mapping(target = "removable", ignore = true)
    void convertFromRequest(DepartmentRequest departmentRequest, @MappingTarget Department department);
}
