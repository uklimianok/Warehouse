package com.warehouse.demo.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.pickedProduct.PickedProductRequest;
import com.warehouse.demo.entity.order.PickedProduct;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.order.PickedProductRepository;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.PickedProductService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PickedProductServiceImpl extends AbstractService<PickedProduct, Long> implements PickedProductService {
    private final PickedProductRepository pickedProductRepository;
    private final OrderPalletRepository orderPalletRepository;
    private final ProductPackageRepository productPackageRepository;

    @Override
    public PickedProduct create(PickedProductRequest pickedProductRequest) {
        PickedProduct pickedProduct = new PickedProduct();

        return modifyAndSave(pickedProduct, pickedProductRequest);
    }

    @Override
    public PickedProduct update(long id, PickedProductRequest pickedProductRequest) {
        PickedProduct pickedProduct = read(id);

        return modifyAndSave(pickedProduct, pickedProductRequest);
    }

    @Override
    protected JpaRepository<PickedProduct, Long> getRepository() {
        return pickedProductRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.PICKED_PRODUCT;
    }

    @Override
    protected boolean isUsed(Long id) {
        return false;
    }
    
    private PickedProduct modifyAndSave(PickedProduct target, PickedProductRequest from) {
        target.setPickedVolume(from.getPickedVolume());

        target.setOrderPallet(
            orderPalletRepository.findById(from.getOrderPalletId())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.ORDER_PALLET, OutputMessage.NOT_FOUND))
            )
        );
        target.setProductPackage(
            productPackageRepository.findById(from.getProductPackageId())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.PRODUCT_PACKAGE, OutputMessage.NOT_FOUND))
            )
        );

        return pickedProductRepository.save(target);
    }
}
