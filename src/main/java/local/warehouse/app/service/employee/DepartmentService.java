package local.warehouse.app.service.employee;

import local.warehouse.app.dto.employee.department.DepartmentRequest;
import local.warehouse.app.entity.employee.Department;
import local.warehouse.app.service.BaseService;

public interface DepartmentService extends BaseService<Department, Long> {
    Department create(DepartmentRequest departmentRequest);
    Department update(long id, DepartmentRequest departmentRequest);
}
