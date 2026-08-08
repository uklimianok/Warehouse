package com.warehouse.demo.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.employee.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByPositionId(long positionId);
    boolean existsByOrganizationId(long organizationId);
    boolean existsByShiftId(long shiftId);
    boolean existsByEmployeeNumber(String employeeNumber);
}
