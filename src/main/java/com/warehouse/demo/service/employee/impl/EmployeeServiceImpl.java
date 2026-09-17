package com.warehouse.demo.service.employee.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.entity.user.User;
import com.warehouse.demo.mapper.employee.EmployeeRequestMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.repository.service.ActionLogRepository;
import com.warehouse.demo.repository.user.UserRepository;
import com.warehouse.demo.repository.workplace.GateRepository;
import com.warehouse.demo.repository.workplace.WorkshopRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.EmployeeService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Department;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends AbstractService<Employee, Long> implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ActionLogRepository actionLogRepository;
    private final PositionRepository positionRepository;
    private final WorkshopRepository workshopRepository;
    private final GateRepository gateRepository;

    private final EmployeeRequestMapper employeeRequestMapper;

    @Value("${warehouse.shared-password}")
    private String password;
    private final PasswordEncoder passwordEncoder;

    @Override 
    @Cacheable(value = "employees", key = "#id")
    public Employee read(Long id) {
        return super.read(id);
    }

    @Override
    @Transactional
    public Employee create(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setEmployeeNumber(generateEmployeeNumber(employeeRequest));
        configureWorkshopAndGate(employee, employeeRequest, true);

        employeeRequestMapper.convertFromRequest(employeeRequest, employee);

        Employee savedEmployee = employeeRepository.save(employee);
        if (savedEmployee.getPosition().isHasDatabaseAccess()) 
            configureUser(savedEmployee);

        return savedEmployee;
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", key = "#id")
    public Employee update(long id, EmployeeRequest employeeRequest, UserPrincipal userPrincipal) {
        Employee employee = read(id);
        boolean DBAccessModeBefore = employee.getPosition().isHasDatabaseAccess();

        throwIfPositionNotConfigurable(employee, userPrincipal);
        configureWorkshopAndGate(employee, employeeRequest, false);

        String department = userPrincipal.getUser().getEmployee().getPosition().getDepartment();
        if (!department.equals(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT))
            employeeRequestMapper.convertFromRequest(employeeRequest, employee);
        else
            employeeRequestMapper.convertFromWarehouseEmployeeDepartmentRequest(employeeRequest, employee);

        Employee savedEmployee = employeeRepository.save(employee);
        boolean DBAccessModeAfter = savedEmployee.getPosition().isHasDatabaseAccess();

        boolean DBAccessChanged = DBAccessModeBefore != DBAccessModeAfter;
        if (DBAccessChanged) {
            if (savedEmployee.getPosition().isHasDatabaseAccess()) configureUser(savedEmployee);
            else userRepository.deleteByEmployeeId(id);
        }

        return savedEmployee;
    }

    @Override 
    @CacheEvict(value = "employees", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    protected JpaRepository<Employee, Long> getRepository() {
        return employeeRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.EMPLOYEE;
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

    private void configureUser(Employee target) {
        User user = new User();
        user.setEmployee(target);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private void throwIfPositionNotConfigurable(Employee object, UserPrincipal subject) {
        Position subjectPosition = subject.getUser().getEmployee().getPosition(); // 1. Check department
        Set<String> allowedDepartments = Set.of(
            Department.WAREHOUSE_EMPLOYEES_DEPARTMENT,
            Department.AUXILIARY_EMPLOYEES_DEPARTMENT,
            Department.HR_DEPARTMENT,
            Department.IT_DEPARTMENT
        );
        if (!allowedDepartments.contains(subjectPosition.getDepartment())) 
            throw new DataIntegrityViolationException("Access denied.");

        if (    // 2. Check whether subject can configure not self
            !subject.getUser().getEmployee().getEmployeeNumber().equals(object.getEmployeeNumber())
            && subjectPosition.getDepartment().equals(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT)
        ) throw new DataIntegrityViolationException("Access denied.");  // Department.WAREHOUSE_EMPLOYEES_DEPARTMENT can change only themselves

        Position objectPosition = object.getPosition();     // 3. Check position priority
        int subjectPriority = Department.PRIORITIES.getOrDefault(subjectPosition.getDepartment(), 0);
        int objectPriority = Department.PRIORITIES.getOrDefault(objectPosition.getDepartment(), 0);
        if (subjectPriority == 0 || objectPriority == 0)
            throw new EntityNotFoundException(Utility.getOutputMessage(Entity.DEPARTMENT, OutputMessage.NOT_FOUND));
        if (subjectPriority > objectPriority)
            throw new DataIntegrityViolationException("Operation denied.");
    }

    private void configureWorkshopAndGate(Employee target, EmployeeRequest from, boolean isCreated) {
        if (isCreated) {
            target.setWorkshop(null);
            target.setGate(null);
            return;
        }

        Position position = positionRepository.findById(from.getPositionId())
            .orElseThrow(() -> new DataIntegrityViolationException(Utility.getOutputMessage(Entity.POSITION, OutputMessage.NOT_FOUND)));
        if (
            position.getCodeName().equals("GOODS_PICKER")
            || position.getCodeName().equals("OPERATOR")
        ) target.setWorkshop(workshopRepository.findById(from.getWorkshopId())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.WORKSHOP, OutputMessage.NOT_FOUND))));
        else if (
            position.getCodeName().equals("GOODS_UNLOADER")
            || position.getCodeName().equals("SET_GOODS_LOADER")
        ) target.setGate(gateRepository.findById(from.getGateId())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.GATE, OutputMessage.NOT_FOUND))));
    }
}
