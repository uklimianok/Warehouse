package com.warehouse.demo.mapper.product;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.mapper.item.pallet.PalletResolver;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageResolver;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletRequestMapper;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletRequestMapperImpl;
import com.warehouse.demo.mapper.service.status.StatusResolver;
import com.warehouse.demo.mapper.workplace.workStation.WorkStationResolver;
import com.warehouse.demo.repository.item.PalletRepository;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.repository.workplace.WorkStationRepository;

public class ProductPalletRequestMapperTest {
    private final WorkStationRepository workStationRepository = mock(WorkStationRepository.class);
    private final PalletRepository palletRepository = mock(PalletRepository.class);
    private final StatusRepository statusRepository = mock(StatusRepository.class);
    private final ProductPackageRepository productPackageRepository = mock(ProductPackageRepository.class);

    private final WorkStationResolver workStationResolver = new WorkStationResolver(workStationRepository);
    private final PalletResolver palletResolver = new PalletResolver(palletRepository);
    private final StatusResolver statusResolver = new StatusResolver(statusRepository);
    private final ProductPackageResolver productPackageResolver = new ProductPackageResolver(productPackageRepository);

    private final ProductPalletRequestMapper productPalletRequestMapper = new ProductPalletRequestMapperImpl(productPackageResolver, palletResolver, statusResolver, workStationResolver);

    @Test 
    void convertFromRequest_nullNextWorkStationId_setsNull() {
        ProductPalletRequest request = generate();

        stub();

        ProductPallet productPallet = new ProductPallet();
        productPalletRequestMapper.convertFromRequest(request, productPallet);

        assertNull(productPallet.getNextWorkStation());
        verify(workStationRepository, times(1)).findById(1L);    // Only called once, for workStationId
    }

    @Test 
    void convertFromRequest_notNullNextWorkStationId_callsResolverTwice() {
        ProductPalletRequest request = generate(2L);

        stub();
        when(workStationRepository.findById(2L)).thenReturn(Optional.of(new WorkStation()));

        ProductPallet productPallet = new ProductPallet();
        productPalletRequestMapper.convertFromRequest(request, productPallet);

        assertNotNull(productPallet.getNextWorkStation());
        verify(workStationRepository, times(1)).findById(1L);   // workStationId
        verify(workStationRepository, times(1)).findById(2L);   // nextWorkStationId
    }

    private ProductPalletRequest generate() {
        return generate(1, 10, 1L, "0000001", "001", 1, 1L, null);
    }

    private ProductPalletRequest generate(Long nextWorkStationId) {
        return generate(1, 10, 1L, "0000001", "001", 1, 1L, nextWorkStationId);
    }

    private ProductPalletRequest generate(long productPackageId, int packageAmount, Long palletId, String palletNumber, String groupNumber, long statusId, Long workStationId, Long nextWorkStationId) {
        ProductPalletRequest request = new ProductPalletRequest();
        request.setProductPackageId(productPackageId);
        request.setPackageAmount(packageAmount);
        request.setPalletId(palletId);
        request.setPalletNumber(palletNumber);
        request.setGroupNumber(groupNumber);
        request.setStatusId(statusId);
        request.setWorkStationId(workStationId);
        request.setNextWorkStationId(nextWorkStationId);

        return request;
    }

    private void stub() {
        when(productPackageRepository.findById(1L)).thenReturn(Optional.of(new ProductPackage()));
        when(palletRepository.findById(1L)).thenReturn(Optional.of(new Pallet()));
        when(statusRepository.findById(1L)).thenReturn(Optional.of(new Status()));
        when(workStationRepository.findById(1L)).thenReturn(Optional.of(new WorkStation()));
    }
}
