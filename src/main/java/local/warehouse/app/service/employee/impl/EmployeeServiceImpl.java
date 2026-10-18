package local.warehouse.app.service.employee.impl;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.configuration.security.UserPrincipal;
import local.warehouse.app.configuration.security.keycloak.service.KeycloakUserService;
import local.warehouse.app.dto.employee.EmployeeRequest;
import local.warehouse.app.dto.product.productPallet.ProductPalletResponse;
import local.warehouse.app.entity.employee.Employee;
import local.warehouse.app.entity.employee.Position;
import local.warehouse.app.entity.product.ProductPallet;
import local.warehouse.app.entity.service.Status;
import local.warehouse.app.event.employee.EmployeeUpdatedEvent;
import local.warehouse.app.mapper.employee.EmployeeRequestMapper;
import local.warehouse.app.mapper.product.productPallet.ProductPalletResponseMapper;
import local.warehouse.app.repository.employee.EmployeeRepository;
import local.warehouse.app.repository.employee.PositionRepository;
import local.warehouse.app.repository.product.ProductPalletRepository;
import local.warehouse.app.repository.service.ActionLogRepository;
import local.warehouse.app.repository.service.StatusRepository;
import local.warehouse.app.repository.workplace.GateRepository;
import local.warehouse.app.repository.workplace.WorkshopRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.employee.EmployeeService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.action.PositionInheritanceTree;
import local.warehouse.app.util.info.DepartmentCodeNames;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import local.warehouse.app.util.info.StatusInfo;

import org.springframework.transaction.annotation.Transactional;
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

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
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

        applicationEventPublisher.publishEvent(new EmployeeUpdatedEvent(savedEmployee.getEmployeeNumber()));    // Calls sendPendingNotifications() if it's successfully commits

        Position newPosition = savedEmployee.getPosition();
        if (oldPosition.getId() != newPosition.getId())
            keycloakUserService.updatePositionAtUser(oldPosition, employee);

        return savedEmployee;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String employeeNumber = employeeRepository.findEmployeeNumberById(id)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));

        super.delete(id);
        employeeRepository.flush(); // Commits delete() at once, not later

        keycloakUserService.deleteUser(employeeNumber);
    }

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
        target.setEmployeeNumber(String.format("%08d", employeeRepository.nextEmployeeNumber()));
    }

    private void throwIfPositionNotConfigurable(Employee caller, Employee object, EmployeeRequest objectRequest) {
        Position callerPosition = caller.getPosition();

        if (    // 1. Check whether subject can configure not self
            !caller.getEmployeeNumber().equals(object.getEmployeeNumber())
            && callerPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.WAREHOUSE_EMPLOYEES_DEPARTMENT)
        ) throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));  // Department.WAREHOUSE_EMPLOYEES_DEPARTMENT can change only themselves

        Position objectPosition = object.getPosition(); // 2. Check position priority
        Position assigningPosition = positionRepository.findById(objectRequest.getPositionId())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.POSITION, OutputMessage.NOT_FOUND)));
        int callerPriority = callerPosition.getDepartment().getPriority();
        int objectPriority = objectPosition.getDepartment().getPriority();
        int assigningPriority = assigningPosition.getDepartment().getPriority();
        if (
            callerPriority > objectPriority
            || callerPriority > assigningPriority
        )   // Subject must have the priority number not less than the object's one 
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
    }

    private void configureWorkshopAndGate(Employee target) {
        target.setWorkshop(null);
        target.setGate(null);
    }

    private void configureWorkshopAndGate(Employee target, EmployeeRequest from) {
        Position position = positionRepository.findById(from.getPositionId())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.POSITION, OutputMessage.NOT_FOUND)));
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
