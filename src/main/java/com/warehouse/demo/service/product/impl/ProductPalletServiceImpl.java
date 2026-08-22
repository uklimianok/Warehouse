package com.warehouse.demo.service.product.impl;

import java.util.Arrays;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.repository.item.PalletRepository;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.product.ProductPalletService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.StatusInfo;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductPalletServiceImpl extends AbstractService<ProductPallet, Long> implements ProductPalletService {
    private final ProductPalletRepository productPalletRepository;
    private final ProductPackageRepository productPackageRepository;
    private final PalletRepository palletRepository;
    private final StatusRepository statusRepository;
    private final WorkStationRepository workStationRepository;

    @Override
    public ProductPallet create(ProductPalletRequest productPalletRequest) {
        if (productPalletRepository.existsByPalletNumber(productPalletRequest.getPalletNumber()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        ProductPallet productPallet = new ProductPallet();
        productPallet.setStatus(statusRepository
            .findByNameAndType(StatusInfo.ProductPalletStatus.ORDERED.getName(), EntityName.PRODUCT_PALLET.getName())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.STATUS, OutputMessage.NOT_FOUND)))    
        );
        productPallet.setWorkStation(null);
        productPallet.setNextWorkStation(null);

        return modifyAndSave(productPallet, productPalletRequest);
    }

    @Override
    public ProductPallet update(long id, ProductPalletRequest productPalletRequest) {
        ProductPallet productPallet = read(id);
        
        boolean productPalletChanged = !productPallet.getPalletNumber().equals(productPalletRequest.getPalletNumber());
        boolean productPalletExists = productPalletRepository.existsByPalletNumber(productPalletRequest.getPalletNumber());
        if (productPalletChanged && productPalletExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        productPallet.setStatus(
            statusRepository.findByIdAndType(productPalletRequest.getStatusId(), EntityName.PRODUCT_PALLET.getName())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.STATUS, OutputMessage.NOT_FOUND)))
        );

        StatusInfo.ProductPalletStatus status = Arrays.stream(StatusInfo.ProductPalletStatus.values())
            .filter(s -> s.getName().equals(productPallet.getStatus().getName()))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.STATUS, OutputMessage.NOT_FOUND)));
        switch (status) {
            case ORDERED:
                if (productPalletRequest.getWorkStationId() != null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.WORK_STATION_NOT_REQUIRED));
                if (productPalletRequest.getNextWorkStationId() == null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.NEXT_WORK_STATION_REQUIRED));
                break;
            case UPLOADED, STORED:
                if (productPalletRequest.getWorkStationId() == null || productPalletRequest.getNextWorkStationId() == null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.WORK_STATIONS_REQUIRED));
                break;
            case ACTIVE:
                if (productPalletRequest.getWorkStationId() == null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.WORK_STATION_REQUIRED));
                if (productPalletRequest.getNextWorkStationId() != null)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.NEXT_WORK_STATION_NOT_REQUIRED));
                break;
            case OUT_OF_USE:
                if (productPalletRequest.getWorkStationId() != null || productPalletRequest.getNextWorkStationId() != null) 
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.WORK_STATIONS_NOT_REQUIRED));
                break;
        }

        if (productPalletRequest.getWorkStationId() != null)
            productPallet.setWorkStation(
                workStationRepository.findById(productPalletRequest.getWorkStationId())
                    .orElseThrow(() -> 
                        new EntityNotFoundException(Utility.getOutputMessage(EntityName.WORK_STATION, OutputMessage.NOT_FOUND))
                    )
            );
        else 
            productPallet.setWorkStation(null);

        if (productPalletRequest.getNextWorkStationId() != null)
            productPallet.setNextWorkStation(
                workStationRepository.findById(productPalletRequest.getNextWorkStationId())
                    .orElseThrow(() -> 
                        new EntityNotFoundException(Utility.getOutputMessage(EntityName.WORK_STATION, OutputMessage.NOT_FOUND))
                    )
            );
        else
            productPallet.setNextWorkStation(null);

        return modifyAndSave(productPallet, productPalletRequest);
    }

    @Override
    protected JpaRepository<ProductPallet, Long> getRepository() {
        return productPalletRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.PRODUCT_PALLET;
    }

    @Override
    protected boolean isUsed(Long id) {
        return false;
    }

    private ProductPallet modifyAndSave(ProductPallet target, ProductPalletRequest from) {
        target.setPackageAmount(from.getPackageAmount());
        target.setPalletNumber(from.getPalletNumber());
        target.setGroupNumber(from.getGroupNumber());

        if (from.getPalletId() != null)
            target.setPallet(palletRepository.findById(from.getPalletId())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.PALLET, OutputMessage.NOT_FOUND))
                )   
            );

        target.setProductPackage(
            productPackageRepository.findById(from.getProductPackageId())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.PRODUCT_PACKAGE, OutputMessage.NOT_FOUND))
            )  
        );

        return productPalletRepository.save(target);
    }
}
