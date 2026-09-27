package com.warehouse.demo.service.employee;

import com.warehouse.demo.dto.employee.department.DepartmentRequest;
import com.warehouse.demo.entity.employee.Department;
import com.warehouse.demo.service.BaseService;

public interface DepartmentService extends BaseService<Department, Long> {
    Department create(DepartmentRequest departmentRequest);
    Department update(long id, DepartmentRequest departmentRequest);
}
