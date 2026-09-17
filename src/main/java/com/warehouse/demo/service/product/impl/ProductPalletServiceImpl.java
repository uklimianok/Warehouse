package com.warehouse.demo.service.product.impl;

import java.util.Arrays;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletRequestMapper;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.product.ProductPalletService;
import com.warehouse.demo.util.action.Utility;
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

    private static final String WORK_STATION_NOT_REQUIRED = "must not contain current position.";
    private static final String WORK_STATION_REQUIRED = "must contain current position.";
    private static final String NEXT_WORK_STATION_REQUIRED = "must contain next position.";
    private static final String NEXT_WORK_STATION_NOT_REQUIRED = "must not contain next position.";
    private static final String WORK_STATIONS_REQUIRED = "must contain current and next position.";
    private static final String WORK_STATIONS_NOT_REQUIRED = "must not contain any position.";

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
        if (productPalletRepository.existsByPalletNumber(productPalletRequest.getPalletNumber()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        ProductPallet productPallet = new ProductPallet();
        productPallet.setStatus(statusRepository
            .findByNameAndType(StatusInfo.ProductPalletStatus.ORDERED.getName(), Entity.PRODUCT_PALLET.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)))    
        );
        productPallet.setWorkStation(null);
        productPallet.setNextWorkStation(null);

        return modifyAndSave(productPallet, productPalletRequest);
    }

    @Override
    @CacheEvict(value = "productPallets", key = "#id")
    public ProductPallet update(long id, ProductPalletRequest productPalletRequest) {
        ProductPallet productPallet = self.read(id);
        
        boolean productPalletChanged = !productPallet.getPalletNumber().equals(productPalletRequest.getPalletNumber());
        boolean productPalletExists = productPalletRepository.existsByPalletNumber(productPalletRequest.getPalletNumber());
        if (productPalletChanged && productPalletExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        productPallet.setStatus(
            statusRepository.findByIdAndType(productPalletRequest.getStatusId(), Entity.PRODUCT_PALLET.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)))
        );

        StatusInfo.ProductPalletStatus status = Arrays.stream(StatusInfo.ProductPalletStatus.values())
            .filter(s -> s.getName().equals(productPallet.getStatus().getName()))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));
        switch (status) {
            case ORDERED:
                if (productPalletRequest.getWorkStationId() != null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), WORK_STATION_NOT_REQUIRED));
                if (productPalletRequest.getNextWorkStationId() == null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), NEXT_WORK_STATION_REQUIRED));
                break;
            case UNLOADED, STORED:
                if (productPalletRequest.getWorkStationId() == null || productPalletRequest.getNextWorkStationId() == null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), WORK_STATIONS_REQUIRED));
                break;
            case ACTIVE:
                if (productPalletRequest.getWorkStationId() == null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), WORK_STATION_REQUIRED));
                if (productPalletRequest.getNextWorkStationId() != null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), NEXT_WORK_STATION_NOT_REQUIRED));
                break;
            case OUT_OF_USE:
                if (productPalletRequest.getWorkStationId() != null || productPalletRequest.getNextWorkStationId() != null) 
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), WORK_STATIONS_NOT_REQUIRED));
                break;
        }

        if (productPalletRequest.getWorkStationId() != null)
            productPallet.setWorkStation(
                workStationRepository.findById(productPalletRequest.getWorkStationId())
                    .orElseThrow(() -> 
                        new EntityNotFoundException(Utility.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND))
                    )
            );
        else 
            productPallet.setWorkStation(null);

        if (productPalletRequest.getNextWorkStationId() != null)
            productPallet.setNextWorkStation(
                workStationRepository.findById(productPalletRequest.getNextWorkStationId())
                    .orElseThrow(() -> 
                        new EntityNotFoundException(Utility.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND))
                    )
            );
        else
            productPallet.setNextWorkStation(null);

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
}
