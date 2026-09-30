package com.warehouse.demo.mapper.employee.department;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Department;
import com.warehouse.demo.repository.employee.DepartmentRepository;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class DepartmentResolver {
    private final DepartmentRepository departmentRepository;

    public Department mapDepartment(long departmentId) {
        return departmentRepository.findById(departmentId)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.DEPARTMENT, OutputMessage.NOT_FOUND)));
    }
}
