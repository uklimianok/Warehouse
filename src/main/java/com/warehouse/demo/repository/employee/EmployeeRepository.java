package com.warehouse.demo.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.demo.entity.employee.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByPositionId(long positionId);
    boolean existsByEmployerOrganizationId(long employerOrganizationId);
    boolean existsByShiftId(long shiftId);
    boolean existsByEmployeeNumber(String employeeNumber);
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    List<Employee> findAllByPositionCodeNameAndWorkshopId(String positionCodeName, long workshopId);
    List<Employee> findAllByPositionCodeNameAndGateId(String positionCodeName, long gateId);
    List<Employee> findAllByPositionCodeName(String positionCodeName);
    @Query("SELECT e.employeeNumber FROM Employee e WHERE e.id = :id")
    Optional<String> findEmployeeNumberById(long id);
}
