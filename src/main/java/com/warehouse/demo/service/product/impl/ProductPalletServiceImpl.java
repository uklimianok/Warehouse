package com.warehouse.demo.service.product.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.event.product.ProductPalletEvent;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletRequestMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.product.ProductPalletService;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.action.PositionInheritanceTree;
import com.warehouse.demo.util.exception.BusinessRuleException;
import com.warehouse.demo.util.info.DepartmentCodeNames;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;
import com.warehouse.demo.util.info.StatusInfo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductPalletServiceImpl extends AbstractService<ProductPallet, Long> implements ProductPalletService {
    //private final ProductPalletService self;

    private final ProductPalletRepository productPalletRepository;
    private final StatusRepository statusRepository;
    private final WorkStationRepository workStationRepository;
    private final EmployeeRepository employeeRepository;

    private final ProductPalletRequestMapper productPalletRequestMapper;

    private final KafkaTemplate<String, ProductPalletEvent> kafkaTemplate;

    private final PositionInheritanceTree positionInheritanceTree;

    private static final String WORK_STATION_REQUIRED = "must contain current position.";
    private static final String WORK_STATION_NOT_REQUIRED = "must not contain current position.";
    private static final String NEXT_WORK_STATION_NOT_REQUIRED = "must not contain next position.";
    private static final String WORK_STATIONS_REQUIRED = "must contain any position.";
    private static final String WORK_STATIONS_NOT_REQUIRED = "must not contain any position.";

    private static final String UNLOADED_STATUS_NEXT_WORK_STATION_NOT_NULL_EVENT = "product-pallet-unloaded-status-next-work-station-not-null-event";
    private static final String UNLOADED_STATUS_NEXT_WORK_STATION_NULL_EVENT = "product-pallet-unloaded-status-next-work-station-null-event";

    public ProductPalletServiceImpl(
        //@Lazy ProductPalletService self, 
        ProductPalletRepository productPalletRepository, 
        StatusRepository statusRepository, 
        WorkStationRepository workStationRepository, 
        EmployeeRepository employeeRepository,
        ProductPalletRequestMapper productPalletRequestMapper, 
        KafkaTemplate<String, ProductPalletEvent> kafkaTemplate,
        PositionInheritanceTree positionInheritanceTree
    ) {
        //this.self = self;
        this.productPalletRepository = productPalletRepository;
        this.statusRepository = statusRepository;
        this.workStationRepository = workStationRepository;
        this.employeeRepository = employeeRepository;
        this.productPalletRequestMapper = productPalletRequestMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.positionInheritanceTree = positionInheritanceTree;
    }

    @Override
    public ProductPallet read(Long id) {
        return super.read(id);
    }

    @Override
    public ProductPallet create(ProductPalletRequest productPalletRequest) {
        ProductPallet productPallet = new ProductPallet();
        generatePalletNumber(productPallet);
        configureStatus(productPallet);
        configureWorkStations(productPallet);

        return modifyAndSave(productPallet, productPalletRequest);
    }

    @Override
    public ProductPallet update(long id, ProductPalletRequest productPalletRequest, UserPrincipal userPrincipal) {
        ProductPallet productPallet = read(id);
        Employee callerEmployee = employeeRepository.findByEmployeeNumber(userPrincipal.getEmployeeNumber())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));

        throwIfNotConfigurable(productPallet, productPalletRequest, callerEmployee);

        boolean statusChanged = productPallet.getStatus().getId() != productPalletRequest.getStatusId();
        if (statusChanged)
            configureStatus(productPallet, productPalletRequest, callerEmployee);

        configureWorkStations(productPallet, productPalletRequest, callerEmployee, statusChanged);

        ProductPallet savedProductPallet = modifyAndSave(productPallet, productPalletRequest);

        if (
            savedProductPallet.getStatus().getName().equals(StatusInfo.PRODUCT_PALLET_UNLOADED)
            || savedProductPallet.getStatus().getName().equals(StatusInfo.PRODUCT_PALLET_STORED)
        ) {
            if (!statusChanged) {
                Long oldNextWorkStationId = productPallet.getNextWorkStation() == null ? null : productPallet.getNextWorkStation().getId();
                Long newNextWorkStationId = productPalletRequest.getNextWorkStationId();

                int rows = productPalletRepository.updateNextWorkStationIfMatching(id, savedProductPallet.getStatus().getName(), newNextWorkStationId, oldNextWorkStationId);
                if (rows == 0)
                    throw new BusinessRuleException(MessageHandler.getOutputMessage(Entity.NEXT_WORK_STATION, OutputMessage.SET));

                savedProductPallet.setNextWorkStation(
                    newNextWorkStationId == null ?
                    null :
                    workStationRepository.findById(newNextWorkStationId)
                        .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))  
                );
            }

            produceEvent(savedProductPallet);
        }

        return savedProductPallet;
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    protected JpaRepository<ProductPallet, Long> getRepository() {
        return productPalletRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.PRODUCT_PALLET;
    }

    private ProductPallet modifyAndSave(ProductPallet target, ProductPalletRequest from) {
        productPalletRequestMapper.convertFromRequest(from, target);
        return productPalletRepository.save(target);
    }

    private void throwIfNotConfigurable(ProductPallet target, ProductPalletRequest from, Employee subject) {
        Position subjectPosition = subject.getPosition();
        boolean palletChanged = (   // Null-safe check
            (target.getPallet() == null && from.getPalletId() != null)
            || (target.getPallet() != null && from.getPalletId() == null)
            || (target.getPallet() != null && from.getPalletId() != null
            && target.getPallet().getId() != from.getPalletId()) 
        );

        if (
            (  // Field is restricted for GOODS_UNLOADER (unless a certain status) 
                palletChanged
                && (positionInheritanceTree.isOneOrDescendant(subjectPosition, "GOODS_UNLOADER")
                && !target.getStatus().getName().equals(StatusInfo.PRODUCT_PALLET_ORDERED))
            )
        ) throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
    }

    private void generatePalletNumber(ProductPallet target) {
        target.setPalletNumber(String.format("%012d", productPalletRepository.nextPalletNumber()));
    }

    private void configureStatus(ProductPallet target) {
        target.setStatus(statusRepository
            .findByNameAndType(StatusInfo.PRODUCT_PALLET_ORDERED, Entity.PRODUCT_PALLET.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)))
        );
    }

    private void configureStatus(ProductPallet target, ProductPalletRequest from, Employee subject) {
        Position subjectPosition = subject.getPosition();
        Status oldStatus = target.getStatus();
        Status newStatus = statusRepository
            .findById(from.getStatusId())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));

        switch (newStatus.getName()) {
            case StatusInfo.PRODUCT_PALLET_ORDERED: {
                if (
                    subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.IT_DEPARTMENT)
                    || subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.AUXILIARY_EMPLOYEES_DEPARTMENT)
                ) target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;

            case StatusInfo.PRODUCT_PALLET_UNLOADED: {
                if (
                    oldStatus.getName().equals(StatusInfo.PRODUCT_PALLET_ORDERED) 
                    && (positionInheritanceTree.isOneOrDescendant(subjectPosition, "GOODS_UNLOADER")
                    || subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.IT_DEPARTMENT))
                ) target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;
            
            case StatusInfo.PRODUCT_PALLET_STORED: {
                if (
                    oldStatus.getName().equals(StatusInfo.PRODUCT_PALLET_UNLOADED)
                    && (positionInheritanceTree.isOneOrDescendant(subjectPosition, "OPERATOR")
                    || subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.IT_DEPARTMENT))
                ) target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;

            case StatusInfo.PRODUCT_PALLET_ACTIVE: {
                if (
                    oldStatus.getName().equals(StatusInfo.PRODUCT_PALLET_STORED)
                    && (positionInheritanceTree.isOneOrDescendant(subjectPosition, "OPERATOR")
                    || subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.IT_DEPARTMENT))
                ) target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;

            case StatusInfo.PRODUCT_PALLET_OUT_OF_USE: {
                if (
                    oldStatus.getName().equals(StatusInfo.PRODUCT_PALLET_ACTIVE)
                    && (positionInheritanceTree.isOneOrDescendant(subjectPosition, "OPERATOR")
                    || subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.IT_DEPARTMENT)
                    || subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.AUXILIARY_EMPLOYEES_DEPARTMENT))
                ) target.setStatus(newStatus);
                else 
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;

            default:    // Other status types
                throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
        }
    }

    private void configureWorkStations(ProductPallet target) {
        target.setWorkStation(null);
        target.setNextWorkStation(null);
    }

    private void configureWorkStations(ProductPallet target, ProductPalletRequest from, Employee subject, boolean statusChanged) {
        Position subjectPosition = subject.getPosition();
        Status status = target.getStatus(); // Already configured in configureStatus() method (i.e. new status)

        switch (status.getName()) {
            case StatusInfo.PRODUCT_PALLET_ORDERED: {
                if (from.getWorkStationId() != null)
                    throw new BusinessRuleException(MessageHandler.getOutputMessage(WORK_STATION_NOT_REQUIRED));

                target.setWorkStation(null);
                target.setNextWorkStation(
                    from.getNextWorkStationId() == null ?
                    null :
                    workStationRepository.findById(from.getNextWorkStationId())
                        .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                );
            }
                break;

            case StatusInfo.PRODUCT_PALLET_UNLOADED, StatusInfo.PRODUCT_PALLET_STORED: {
                if (!statusChanged) {
                    if (from.getWorkStationId() == null)
                        throw new BusinessRuleException(MessageHandler.getOutputMessage(WORK_STATION_REQUIRED));

                    target.setWorkStation(workStationRepository.findById(from.getWorkStationId())
                        .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                    );
                } else {
                    if (
                        (status.getName().equals(StatusInfo.PRODUCT_PALLET_UNLOADED)
                        && from.getWorkStationId() == null)
                        || (status.getName().equals(StatusInfo.PRODUCT_PALLET_STORED)
                        && (from.getWorkStationId() == null || from.getNextWorkStationId() == null))
                    ) throw new BusinessRuleException(MessageHandler.getOutputMessage(WORK_STATIONS_REQUIRED));

                    if (    // GOODS_UNLOADER can change only at UNLOADED and OPERATOR only at STORED
                        (!status.getName().equals(StatusInfo.PRODUCT_PALLET_UNLOADED)
                        && positionInheritanceTree.isOneOrDescendant(subjectPosition, "GOODS_UNLOADER"))
                        || (!status.getName().equals(StatusInfo.PRODUCT_PALLET_STORED)
                        && positionInheritanceTree.isOneOrDescendant(subjectPosition, "OPERATOR"))
                    ) throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));

                    if (
                        positionInheritanceTree.isOneOrDescendant(subjectPosition, "GOODS_UNLOADER")
                        || positionInheritanceTree.isOneOrDescendant(subjectPosition, "OPERATOR")
                    ) {   // Auto-transition
                        target.setWorkStation(target.getNextWorkStation());
                        target.setNextWorkStation(null);
                    } else {
                        target.setWorkStation(workStationRepository.findById(from.getWorkStationId())
                            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                        );
                        target.setNextWorkStation(
                            from.getNextWorkStationId() == null ?
                            null :
                            workStationRepository.findById(from.getNextWorkStationId())
                            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                        );
                    }
                }
            }
                break;
            
            case StatusInfo.PRODUCT_PALLET_ACTIVE: {
                if (from.getWorkStationId() == null)
                    throw new BusinessRuleException(MessageHandler.getOutputMessage(WORK_STATION_REQUIRED));
                if (from.getNextWorkStationId() != null)
                    throw new BusinessRuleException(MessageHandler.getOutputMessage(NEXT_WORK_STATION_NOT_REQUIRED));

                if (positionInheritanceTree.isOneOrDescendant(subjectPosition, "OPERATOR")) {   // Auto-transition
                    target.setWorkStation(target.getNextWorkStation());
                    target.setNextWorkStation(null);
                } else {
                    target.setWorkStation(workStationRepository.findById(from.getWorkStationId())
                        .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                    );
                    target.setNextWorkStation(null);
                }
            }
                break;

            case StatusInfo.PRODUCT_PALLET_OUT_OF_USE: {
                if (from.getWorkStationId() != null || from.getNextWorkStationId() != null) 
                    throw new BusinessRuleException(MessageHandler.getOutputMessage(WORK_STATIONS_NOT_REQUIRED));
            
                configureWorkStations(target);
            }
                break;

            default:
                throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
        }
    }

    private void produceEvent(ProductPallet entity) {
        ProductPalletEvent productPalletEvent = new ProductPalletEvent();
        productPalletEvent.setProductPackageId(entity.getProductPackage().getId());
        productPalletEvent.setPalletNumber(entity.getPalletNumber());
        productPalletEvent.setGroupNumber(entity.getGroupNumber());
        productPalletEvent.setWorkStationId(
            entity.getWorkStation() == null ?
            null :
            entity.getWorkStation().getId()
        );
        productPalletEvent.setNextWorkStationId(
            entity.getNextWorkStation() == null ?
            null :
            entity.getNextWorkStation().getId()
        );

        if (entity.getStatus().getName().equals(StatusInfo.PRODUCT_PALLET_UNLOADED)) {
            if (productPalletEvent.getNextWorkStationId() != null)
                kafkaTemplate.send(UNLOADED_STATUS_NEXT_WORK_STATION_NOT_NULL_EVENT, productPalletEvent);
            else
                kafkaTemplate.send(UNLOADED_STATUS_NEXT_WORK_STATION_NULL_EVENT, productPalletEvent);
        }
    }
}
