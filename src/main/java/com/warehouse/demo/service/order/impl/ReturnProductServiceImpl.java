package com.warehouse.demo.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.returnProduct.ReturnProductRequest;
import com.warehouse.demo.entity.order.ReturnProduct;
import com.warehouse.demo.mapper.order.returnProduct.ReturnProductRequestMapper;
import com.warehouse.demo.repository.order.ReturnProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.ReturnProductService;
import com.warehouse.demo.util.EntityName;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnProductServiceImpl extends AbstractService<ReturnProduct, Long> implements ReturnProductService {
    private final ReturnProductRepository returnProductRepository;

    private final ReturnProductRequestMapper returnProductRequestMapper;

    @Override
    public ReturnProduct create(ReturnProductRequest returnProductRequest) {
        ReturnProduct returnProduct = new ReturnProduct();

        return modifyAndSave(returnProduct, returnProductRequest);
    }

    @Override
    public ReturnProduct update(long id, ReturnProductRequest returnProductRequest) {
        ReturnProduct returnProduct = read(id);

        return modifyAndSave(returnProduct, returnProductRequest);
    }

    @Override
    protected JpaRepository<ReturnProduct, Long> getRepository() {
        return returnProductRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.RETURN_PRODUCT;
    }

    private ReturnProduct modifyAndSave(ReturnProduct target, ReturnProductRequest from) {
        returnProductRequestMapper.convertFromRequest(from, target);
        return returnProductRepository.save(target);
    }
}
