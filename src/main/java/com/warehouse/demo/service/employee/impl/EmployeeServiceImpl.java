package com.warehouse.demo.service.employee.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.user.User;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.repository.employee.ShiftRepository;
import com.warehouse.demo.repository.service.ActionLogRepository;
import com.warehouse.demo.repository.user.UserRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.EmployeeService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends AbstractService<Employee, Long> implements EmployeeService {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final PositionRepository positionRepository;
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final ActionLogRepository actionLogRepository;

    @Value("${warehouse.shared-password}")
    private String password;

    @Override
    @Transactional
    public Employee create(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setEmployeeNumber(generateEmployeeNumber(employeeRequest));

        Employee savedEmployee = modifyAndSave(employee, employeeRequest);
        if (savedEmployee.getPosition().isHasDatabaseAccess()) {
            User user = new User();
            user.setEmployee(savedEmployee);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }

        return savedEmployee;
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
    protected EntityName getEntityName() {
        return EntityName.EMPLOYEE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInUser = userRepository.existsByEmployeeId(id);
        boolean activeInActionLog = actionLogRepository.existsByEmployeeId(id);
        return activeInUser || activeInActionLog;
    }

    private String generateEmployeeNumber(EmployeeRequest employee) {
        long positionId = employee.getPositionId();
        if (positionId < 0 || positionId > 99)
            throw new DataIntegrityViolationException("Impossible to create employee number.");

        int lastBirthDigit = employee.getBirthDate().getDayOfMonth() % 10;

        long count = employeeRepository.count() + 1;
        if (count > 99999)
            throw new DataIntegrityViolationException("Impossible to create employee number.");

        return String.format("%02d%01d%05d", positionId, lastBirthDigit, count);
    }

    private Employee modifyAndSave(Employee target, EmployeeRequest from) {
        target.setFirstName(from.getFirstName());
        target.setLastName(from.getLastName());
        target.setBirthDate(from.getBirthDate());
        target.setDocumentId(from.getDocumentId());
        target.setResidenceAddress(from.getResidenceAddress());
        target.setPhoneNumber(from.getPhoneNumber());
        target.setEmployerOrganization(
            organizationRepository.findById(
                from.getEmployerOrganizationId()
            ).orElseThrow(
                () -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.ORGANIZATION, OutputMessage.NOT_FOUND))
            )
        );
        target.setPosition(
            positionRepository.findById(
                from.getPositionId()
            ).orElseThrow(
                () -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.POSITION, OutputMessage.NOT_FOUND))
            )
        );
        target.setShift(
            shiftRepository.findById(
                from.getShiftId()
            ).orElseThrow(
                () -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.SHIFT, OutputMessage.NOT_FOUND))
            )
        );

        return employeeRepository.save(target);
    }
}
