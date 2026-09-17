package com.warehouse.demo.mapper.employee;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeResolver {
    private final EmployeeRepository employeeRepository;

    public Employee mapEmployee(long employeeId) {
        return employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));
    }
}
