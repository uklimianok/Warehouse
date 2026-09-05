package com.warehouse.demo.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.pickedProduct.PickedProductRequest;
import com.warehouse.demo.entity.order.PickedProduct;
import com.warehouse.demo.mapper.order.pickedProduct.PickedProductRequestMapper;
import com.warehouse.demo.repository.order.PickedProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.PickedProductService;
import com.warehouse.demo.util.EntityName;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PickedProductServiceImpl extends AbstractService<PickedProduct, Long> implements PickedProductService {
    private final PickedProductRepository pickedProductRepository;

    private final PickedProductRequestMapper pickedProductRequestMapper;

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
    
    private PickedProduct modifyAndSave(PickedProduct target, PickedProductRequest from) {
        pickedProductRequestMapper.convertFromRequest(from, target);
        return pickedProductRepository.save(target);
    }
}
