package com.warehouse.demo.service.employee;

import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.service.BaseService;

public interface EmployeeService extends BaseService<Employee, Long> {
    Employee create(EmployeeRequest employeeRequest);
    Employee update(long id, EmployeeRequest employeeRequest);
}
