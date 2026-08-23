package com.warehouse.demo.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.returnProduct.ReturnProductRequest;
import com.warehouse.demo.entity.order.ReturnProduct;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.order.ReturnProductRepository;
import com.warehouse.demo.repository.product.ProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.ReturnProductService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnProductServiceImpl extends AbstractService<ReturnProduct, Long> implements ReturnProductService {
    private final ReturnProductRepository returnProductRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

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
        target.setProductsAmount(from.getProductsAmount());

        target.setOrder(
            orderRepository.findById(from.getOrderId())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.ORDER, OutputMessage.NOT_FOUND))
            )
        );
        target.setProduct(
            productRepository.findById(from.getProductId())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.PRODUCT, OutputMessage.NOT_FOUND))
            )     
        );

        return returnProductRepository.save(target);
    }
}
