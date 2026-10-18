package local.warehouse.app.service.employee;

import local.warehouse.app.configuration.security.UserPrincipal;
import local.warehouse.app.dto.employee.EmployeeRequest;
import local.warehouse.app.entity.employee.Employee;
import local.warehouse.app.service.BaseService;

public interface EmployeeService extends BaseService<Employee, Long> {
    Employee create(EmployeeRequest employeeRequest);
    Employee update(long id, EmployeeRequest employeeRequest, UserPrincipal userPrincipal);
    void sendPendingNotifications(String to);
}
