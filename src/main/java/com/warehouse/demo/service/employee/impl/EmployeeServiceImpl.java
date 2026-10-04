package com.warehouse.demo.service.employee.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.configuration.security.keycloak.service.KeycloakUserService;
import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.dto.product.productPallet.ProductPalletResponse;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.mapper.employee.EmployeeRequestMapper;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletResponseMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.service.ActionLogRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.repository.workplace.GateRepository;
import com.warehouse.demo.repository.workplace.WorkshopRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.EmployeeService;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.action.PositionInheritanceTree;
import com.warehouse.demo.util.info.DepartmentCodeNames;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;
import com.warehouse.demo.util.info.StatusInfo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends AbstractService<Employee, Long> implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ActionLogRepository actionLogRepository;
    private final PositionRepository positionRepository;
    private final WorkshopRepository workshopRepository;
    private final GateRepository gateRepository;
    private final ProductPalletRepository productPalletRepository;
    private final StatusRepository statusRepository;

    private final KeycloakUserService keycloakUserService;

    private final EmployeeRequestMapper employeeRequestMapper;
    private final ProductPalletResponseMapper productPalletResponseMapper;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final PositionInheritanceTree positionInheritanceTree;

    private static AtomicLong EMPLOYEE_NUMBER_COUNTER = new AtomicLong(0);

    @Override 
    @Cacheable(value = "employees", key = "#id")
    public Employee read(Long id) {
        return super.read(id);
    }

    @Override
    @Transactional
    public Employee create(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        generateEmployeeNumber(employee);
        configureWorkshopAndGate(employee);

        employeeRequestMapper.convertFromRequest(employeeRequest, employee);

        Employee savedEmployee = employeeRepository.saveAndFlush(employee); // save() doesn't fully save the entity at once, so that it may conflict with Keycloak functionality
        if (savedEmployee.getPosition().isEnabled()) 
            keycloakUserService.createUser(savedEmployee);

        return savedEmployee;
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", key = "#id")
    public Employee update(long id, EmployeeRequest employeeRequest, UserPrincipal userPrincipal) {
        Employee employee = read(id);
        Position oldPosition = employee.getPosition();

        Employee callerEmployee = employeeRepository.findByEmployeeNumber(userPrincipal.getEmployeeNumber())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));

        throwIfPositionNotConfigurable(callerEmployee, employee, employeeRequest);
        configureWorkshopAndGate(employee, employeeRequest);

        String department = callerEmployee.getPosition().getDepartment().getCodeName();
        if (!department.equals(DepartmentCodeNames.WAREHOUSE_EMPLOYEES_DEPARTMENT))
            employeeRequestMapper.convertFromRequest(employeeRequest, employee);
        else
            employeeRequestMapper.convertFromWarehouseEmployeeDepartmentRequest(employeeRequest, employee);

        Employee savedEmployee = employeeRepository.save(employee);

        sendPendingNotifications(savedEmployee.getEmployeeNumber());

        Position newPosition = savedEmployee.getPosition();
        if (oldPosition.getId() != newPosition.getId())
            keycloakUserService.updatePositionAtUser(oldPosition, employee);

        return savedEmployee;
    }

    @Override 
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public void delete(Long id) {
        String employeeNumber = employeeRepository.findEmployeeNumberById(id)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));

        super.delete(id);
        employeeRepository.flush(); // Commits delete() at once, not later

        keycloakUserService.deleteUser(employeeNumber);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPendingNotifications(String to) {
        Employee employee = employeeRepository.findByEmployeeNumber(to)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));

        if (positionInheritanceTree.isOneOrDescendant(employee.getPosition(), "DATA_CONTROLLER")) {
            Status status = statusRepository.findByNameAndType(StatusInfo.PRODUCT_PALLET_UNLOADED, Entity.PRODUCT_PALLET.getEntity())
                .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));
            List<ProductPallet> productPallets = productPalletRepository.findAllByStatusEqualsAndNextWorkStationIsNull(status);
            List<? extends ProductPalletResponse> productPalletsResponse = productPallets.stream()
                .map(pp -> productPalletResponseMapper.convertToTransferResponse(pp))
                .toList();

            simpMessagingTemplate.convertAndSendToUser(
                to, 
                "/queue/notify", 
                productPalletsResponse
            );
        }

        if (positionInheritanceTree.isOneOrDescendant(employee.getPosition(), "OPERATOR") && employee.getWorkshop() != null) {
            Status status = statusRepository.findByNameAndType(StatusInfo.PRODUCT_PALLET_UNLOADED, Entity.PRODUCT_PALLET.getEntity())
                .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));
            List<ProductPallet> productPallets = productPalletRepository.findAllByStatusEqualsAndNextWorkStationWorkshopId(status, employee.getWorkshop().getId());
            List<? extends ProductPalletResponse> productPalletsResponse = productPallets.stream()
                .map(pp -> productPalletResponseMapper.convertToTransferResponse(pp))
                .toList();

            simpMessagingTemplate.convertAndSendToUser(
                to, 
                "/queue/notify", 
                productPalletsResponse
            );
        }
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
        String employeeNumber = employeeRepository.findEmployeeNumberById(id)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));
        boolean activeInActionLog = actionLogRepository.existsByEmployeeNumber(employeeNumber);
        return activeInActionLog;
    }

    private void generateEmployeeNumber(Employee target) {
        EMPLOYEE_NUMBER_COUNTER.set(employeeRepository.getNextIdValue() + 10_000_000);
        target.setEmployeeNumber(String.valueOf(EMPLOYEE_NUMBER_COUNTER.get()));
    }

    private void throwIfPositionNotConfigurable(Employee subject, Employee object, EmployeeRequest objectRequest) {
        Position subjectPosition = subject.getPosition();   // Caller

        if (    // 1. Check whether subject can configure not self
            !subject.getEmployeeNumber().equals(object.getEmployeeNumber())
            && subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.WAREHOUSE_EMPLOYEES_DEPARTMENT)
        ) throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));  // Department.WAREHOUSE_EMPLOYEES_DEPARTMENT can change only themselves

        Position objectPosition = positionRepository.findById(objectRequest.getPositionId())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.POSITION, OutputMessage.NOT_FOUND)));     // 2. Check position priority
        int subjectPriority = subjectPosition.getDepartment().getPriority();
        int objectPriority = objectPosition.getDepartment().getPriority();
        if (subjectPriority > objectPriority)   // Subject must have the priority number not less than the object's one 
            throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
    }

    private void configureWorkshopAndGate(Employee target) {
        target.setWorkshop(null);
        target.setGate(null);
    }

    private void configureWorkshopAndGate(Employee target, EmployeeRequest from) {
        Position position = positionRepository.findById(from.getPositionId())
            .orElseThrow(() -> new DataIntegrityViolationException(MessageHandler.getOutputMessage(Entity.POSITION, OutputMessage.NOT_FOUND)));
        if (
            from.getWorkshopId() != null
            && (positionInheritanceTree.isOneOrDescendant(position, "GOODS_PICKER")
            || positionInheritanceTree.isOneOrDescendant(position, "OPERATOR"))
        ) target.setWorkshop(workshopRepository.findById(from.getWorkshopId())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.WORKSHOP, OutputMessage.NOT_FOUND))));
        else if (
            from.getGateId() != null
            && (positionInheritanceTree.isOneOrDescendant(position, "GOODS_UNLOADER")
            || positionInheritanceTree.isOneOrDescendant(position, "SET_GOODS_LOADER"))
        ) target.setGate(gateRepository.findById(from.getGateId())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.GATE, OutputMessage.NOT_FOUND))));
        else configureWorkshopAndGate(target);
    }
}
