package com.warehouse.demo.service.employee.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.user.User;
import com.warehouse.demo.mapper.employee.EmployeeRequestMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.service.ActionLogRepository;
import com.warehouse.demo.repository.user.UserRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.EmployeeService;
import com.warehouse.demo.util.EntityName;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends AbstractService<Employee, Long> implements EmployeeService {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ActionLogRepository actionLogRepository;

    private final EmployeeRequestMapper employeeRequestMapper;

    @Value("${warehouse.shared-password}")
    private String password;

    @Override
    @Transactional
    public Employee create(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setEmployeeNumber(generateEmployeeNumber(employeeRequest));

        Employee savedEmployee = modifyAndSave(employee, employeeRequest);
        if (savedEmployee.getPosition().isHasDatabaseAccess()) configureUser(savedEmployee);

        return savedEmployee;
    }

    @Override
    @Transactional
    public Employee update(long id, EmployeeRequest employeeRequest) {
        Employee employee = read(id);
        boolean DBAccessModeBefore = employee.getPosition().isHasDatabaseAccess();

        Employee savedEmployee = modifyAndSave(employee, employeeRequest);
        boolean DBAccessModeAfter = savedEmployee.getPosition().isHasDatabaseAccess();

        boolean DBAccessChanged = DBAccessModeBefore != DBAccessModeAfter;
        if (DBAccessChanged) {
            if (savedEmployee.getPosition().isHasDatabaseAccess()) configureUser(savedEmployee);
            else userRepository.deleteByEmployeeId(id);
        }

        return savedEmployee;
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

    private Employee modifyAndSave(Employee target, EmployeeRequest from) {
        employeeRequestMapper.convertFromRequest(from, target);
        return employeeRepository.save(target);
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

    private void configureUser(Employee target) {
        User user = new User();
        user.setEmployee(target);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
