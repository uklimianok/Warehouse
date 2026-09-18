package com.warehouse.demo.service.product.impl;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.event.product.ProductPalletEvent;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletRequestMapper;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.product.ProductPalletService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Department;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;
import com.warehouse.demo.util.info.StatusInfo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductPalletServiceImpl extends AbstractService<ProductPallet, Long> implements ProductPalletService {
    private final ProductPalletService self;

    private final ProductPalletRepository productPalletRepository;
    private final StatusRepository statusRepository;
    private final WorkStationRepository workStationRepository;

    private final ProductPalletRequestMapper productPalletRequestMapper;

    private static final String WORK_STATION_REQUIRED = "must contain current position.";
    private static final String WORK_STATION_NOT_REQUIRED = "must not contain current position.";
    private static final String NEXT_WORK_STATION_NOT_REQUIRED = "must not contain next position.";
    private static final String WORK_STATIONS_REQUIRED = "must contain any position.";
    private static final String WORK_STATIONS_NOT_REQUIRED = "must not contain any position.";

    private static AtomicLong PALLET_NUMBER_COUNTER = new AtomicLong(0);

    public ProductPalletServiceImpl(@Lazy ProductPalletService self, ProductPalletRepository productPalletRepository, StatusRepository statusRepository, WorkStationRepository workStationRepository, ProductPalletRequestMapper productPalletRequestMapper) {
        this.self = self;
        this.productPalletRepository = productPalletRepository;
        this.statusRepository = statusRepository;
        this.workStationRepository = workStationRepository;
        this.productPalletRequestMapper = productPalletRequestMapper;
    }

    @Override 
    @Cacheable(value = "productPallets", key = "#id")
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
    @CacheEvict(value = "productPallets", key = "#id")
    public ProductPallet update(long id, ProductPalletRequest productPalletRequest, UserPrincipal userPrincipal) {
        ProductPallet productPallet = self.read(id);

        throwIfNotConfigurable(productPallet, productPalletRequest, userPrincipal);

        boolean statusChanged = productPallet.getStatus().getId() != productPalletRequest.getStatusId();
        if (statusChanged)
            configureStatus(productPallet, productPalletRequest, userPrincipal);

        configureWorkStations(productPallet, productPalletRequest, userPrincipal);

        return modifyAndSave(productPallet, productPalletRequest);
    }

    @Override 
    @CacheEvict(value = "productPallets", key = "#id")
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

    private void throwIfNotConfigurable(ProductPallet target, ProductPalletRequest from, UserPrincipal subject) {
        Position subjectPosition = subject.getUser().getEmployee().getPosition();
        boolean palletChanged = (   // Null-safe check
            (target.getPallet() == null && from.getPalletId() != null)
            || (target.getPallet() != null && from.getPalletId() == null)
            || (target.getPallet() != null && from.getPalletId() != null
            && target.getPallet().getId() != from.getPalletId()) 
        );

        if (
            (   // Condition 1: fields that are absolutely restricted for the roles
                subjectPosition.getDepartment().equals(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT)
                && (target.getProductPackage().getId() != from.getProductPackageId()
                || target.getPackageAmount() != from.getPackageAmount()
                || !target.getGroupNumber().equals(from.getGroupNumber()))
            ) || (  // Condition 2: field is restricted for OPERATOR (absolutely) and GOODS_UNLOADER (unless a certain status) 
                palletChanged
                && (subjectPosition.getCodeName().equals("OPERATOR")
                || (subjectPosition.getCodeName().equals("GOODS_UNLOADER")
                && !target.getStatus().getName().equals(StatusInfo.PRODUCT_PALLET_ORDERED)))
            )
        ) 
            throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_DENIED));
    }

    private void generatePalletNumber(ProductPallet target) {
        boolean palletNumberExists = productPalletRepository.existsByPalletNumber(String.format("%12d", PALLET_NUMBER_COUNTER.incrementAndGet()));
        if (palletNumberExists)
            PALLET_NUMBER_COUNTER.set(productPalletRepository.count() + 1);

        target.setPalletNumber(String.format("%12d", PALLET_NUMBER_COUNTER.get()));
    }

    private void configureStatus(ProductPallet target) {
        target.setStatus(statusRepository
            .findByNameAndType(StatusInfo.PRODUCT_PALLET_ORDERED, Entity.PRODUCT_PALLET.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)))
        );
    }

    private void configureStatus(ProductPallet target, ProductPalletRequest from, UserPrincipal subject) {
        Position subjectPosition = subject.getUser().getEmployee().getPosition();
        Status oldStatus = target.getStatus();
        Status newStatus = statusRepository
            .findById(from.getStatusId())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));

        switch (newStatus.getName()) {
            case StatusInfo.PRODUCT_PALLET_ORDERED: {
                if (
                    subjectPosition.getDepartment().equals(Department.IT_DEPARTMENT)
                    || subjectPosition.getDepartment().equals(Department.AUXILIARY_EMPLOYEES_DEPARTMENT)
                ) target.setStatus(newStatus);
                else
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;

            case StatusInfo.PRODUCT_PALLET_UNLOADED: {
                if (
                    oldStatus.getName().equals(StatusInfo.PRODUCT_PALLET_ORDERED) 
                    && (subjectPosition.getCodeName().equals("GOODS_UNLOADER")
                    || subjectPosition.getDepartment().equals(Department.IT_DEPARTMENT))
                ) target.setStatus(newStatus);
                else
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;
            
            case StatusInfo.PRODUCT_PALLET_STORED: {
                if (
                    oldStatus.getName().equals(StatusInfo.PRODUCT_PALLET_UNLOADED)
                    && (subjectPosition.getCodeName().equals("OPERATOR")
                    || subjectPosition.getDepartment().equals(Department.IT_DEPARTMENT))
                ) target.setStatus(newStatus);
                else
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;

            case StatusInfo.PRODUCT_PALLET_ACTIVE: {
                if (
                    oldStatus.getName().equals(StatusInfo.PRODUCT_PALLET_STORED)
                    && (subjectPosition.getCodeName().equals("OPERATOR")
                    || subjectPosition.getDepartment().equals(Department.IT_DEPARTMENT))
                ) target.setStatus(newStatus);
                else
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;

            case StatusInfo.PRODUCT_PALLET_OUT_OF_USE: {
                if (
                    oldStatus.getName().equals(StatusInfo.PRODUCT_PALLET_ACTIVE)
                    && (subjectPosition.getCodeName().equals("OPERATOR")
                    || subjectPosition.getDepartment().equals(Department.IT_DEPARTMENT)
                    || subjectPosition.getDepartment().equals(Department.AUXILIARY_EMPLOYEES_DEPARTMENT))
                ) target.setStatus(newStatus);
                else 
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
                break;

            default:    // StatusInfo.PRODUCT_PALLET_ORDERED or other types
                throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_DENIED));
        }
    }

    private void configureWorkStations(ProductPallet target) {
        target.setWorkStation(null);
        target.setNextWorkStation(null);
    }

    private void configureWorkStations(ProductPallet target, ProductPalletRequest from, UserPrincipal subject) {
        Position subjectPosition = subject.getUser().getEmployee().getPosition();
        Status status = target.getStatus();

        switch (status.getName()) {
            case StatusInfo.PRODUCT_PALLET_ORDERED: {
                if (from.getWorkStationId() != null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(WORK_STATION_NOT_REQUIRED));

                target.setWorkStation(null);
                target.setNextWorkStation(
                    from.getNextWorkStationId() == null ?
                    null :
                    workStationRepository.findById(from.getNextWorkStationId())
                        .orElseThrow(() -> new DataIntegrityViolationException(Utility.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                );
            }
                break;

            case StatusInfo.PRODUCT_PALLET_UNLOADED, StatusInfo.PRODUCT_PALLET_STORED: {
                if (from.getWorkStationId() == null || from.getNextWorkStationId() == null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(WORK_STATIONS_REQUIRED));

                if (subjectPosition.getCodeName().equals("OPERATOR")) {   // Auto-transition
                    target.setWorkStation(target.getNextWorkStation());
                    target.setNextWorkStation(null);
                } else {
                    target.setWorkStation(workStationRepository.findById(from.getWorkStationId())
                        .orElseThrow(() -> new DataIntegrityViolationException(Utility.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                    );
                    target.setNextWorkStation(workStationRepository.findById(from.getNextWorkStationId())
                        .orElseThrow(() -> new DataIntegrityViolationException(Utility.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                    );
                }
            }
                break;
            
            case StatusInfo.PRODUCT_PALLET_ACTIVE: {
                if (from.getWorkStationId() == null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(WORK_STATION_REQUIRED));
                if (from.getNextWorkStationId() != null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(NEXT_WORK_STATION_NOT_REQUIRED));

                if (subjectPosition.getCodeName().equals("OPERATOR")) {   // Auto-transition
                    target.setWorkStation(target.getNextWorkStation());
                    target.setNextWorkStation(null);
                } else {
                    target.setWorkStation(workStationRepository.findById(from.getWorkStationId())
                        .orElseThrow(() -> new DataIntegrityViolationException(Utility.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)))
                    );
                    target.setNextWorkStation(null);
                }
            }
                break;

            case StatusInfo.PRODUCT_PALLET_OUT_OF_USE: {
                if (from.getWorkStationId() != null && from.getNextWorkStationId() != null) 
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(WORK_STATIONS_NOT_REQUIRED));
            
                configureWorkStations(target);
            }
                break;

            default:
                throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_DENIED));
        }
    }

    @KafkaListener(groupId = "1", topics = "product-pallet-next-work-station-event")
    public void listen(ProductPalletEvent productPalletEvent) {
        // Add logic later
    }
}
