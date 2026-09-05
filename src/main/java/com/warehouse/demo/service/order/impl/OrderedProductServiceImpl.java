package com.warehouse.demo.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.orderedProduct.OrderedProductRequest;
import com.warehouse.demo.entity.order.OrderedProduct;
import com.warehouse.demo.mapper.order.orderedProduct.OrderedProductRequestMapper;
import com.warehouse.demo.repository.order.OrderedProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.OrderedProductService;
import com.warehouse.demo.util.EntityName;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderedProductServiceImpl extends AbstractService<OrderedProduct, Long> implements OrderedProductService {
    private final OrderedProductRepository orderedProductRepository;

    private final OrderedProductRequestMapper orderedProductRequestMapper;

    @Override
    public OrderedProduct create(OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = new OrderedProduct();

        return modifyAndSave(orderedProduct, orderedProductRequest);
    }

    @Override
    public OrderedProduct update(long id, OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = read(id);

        return modifyAndSave(orderedProduct, orderedProductRequest);
    }

    @Override
    protected JpaRepository<OrderedProduct, Long> getRepository() {
        return orderedProductRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.ORDERED_PRODUCT;
    }

    private OrderedProduct modifyAndSave(OrderedProduct target, OrderedProductRequest from) {
        orderedProductRequestMapper.convertFromRequest(from, target);
        return orderedProductRepository.save(target);
    }
}
