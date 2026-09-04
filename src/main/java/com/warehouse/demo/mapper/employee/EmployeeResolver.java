package com.warehouse.demo.mapper.employee;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeResolver {
    private final EmployeeRepository employeeRepository;

    public Employee mapEmployee(long employeeId) {
        return employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.EMPLOYEE, OutputMessage.NOT_FOUND)));
    }
}
