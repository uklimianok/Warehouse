package com.warehouse.demo.service.employee.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.repository.employee.ShiftRepository;
import com.warehouse.demo.service.AbstractCrudService;
import com.warehouse.demo.service.employee.EmployeeService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends AbstractCrudService<Employee, Long> implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final PositionRepository positionRepository;
    private final ShiftRepository shiftRepository;

    @Override
    public Employee create(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        
        String employeeNumber = generateEmployeeNumber(0, employeeRequest);
        int counter = 0;
        while (employeeRepository.existsByEmployeeNumber(employeeNumber) && counter < 1000000) 
            employeeNumber = generateEmployeeNumber(counter++, employeeRequest);
            
        if (counter < 1000000) employee.setEmployeeNumber(employeeNumber);
        else throw new DataIntegrityViolationException("Impossible to create employee number.");

        return modifyAndSave(employee, employeeRequest);
    }

    @Override
    public Employee update(long id, EmployeeRequest employeeRequest) {
        Employee employee = read(id);
        return modifyAndSave(employee, employeeRequest);
    }

    @Override
    protected JpaRepository<Employee, Long> getRepository() {
        return employeeRepository;
    }

    @Override
    protected String getEntityName() {
        return "Employee";
    }

    private String generateEmployeeNumber(int counter, EmployeeRequest from) {  // fix method later
        return counter > 0 ?
        String.format(
            "%02d%06d",
            from.getPositionId(),
            counter
        ) :
        String.format(
            "%02d%01d%05d", 
            from.getPositionId(), 
            from.getBirthDate().getDayOfMonth() % 10,
            (employeeRepository.count() + 1)
        );
    }

    private Employee modifyAndSave(Employee target, EmployeeRequest from) {
        target.setFirstName(from.getFirstName());
        target.setLastName(from.getLastName());
        target.setBirthDate(from.getBirthDate());
        target.setDocumentId(from.getDocumentId());
        target.setResidenceAddress(from.getResidenceAddress());
        target.setPhoneNumber(from.getPhoneNumber());
        target.setOrganization(
            organizationRepository.findById(
                from.getOrganizationId()
            ).orElseThrow(
                () -> new EntityNotFoundException("Organization not found.")
            )
        );
        target.setPosition(
            positionRepository.findById(
                from.getPositionId()
            ).orElseThrow(
                () -> new EntityNotFoundException("Position not found.")
            )
        );
        target.setShift(
            shiftRepository.findById(
                from.getShiftId()
            ).orElseThrow(
                () -> new EntityNotFoundException("Shift not found.")
            )
        );

        return employeeRepository.save(target);
    }
}
