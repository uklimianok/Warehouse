package com.warehouse.demo.service.order.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.orderedProduct.OrderedProductRequest;
import com.warehouse.demo.entity.order.OrderedProduct;
import com.warehouse.demo.mapper.order.orderedProduct.OrderedProductRequestMapper;
import com.warehouse.demo.repository.order.OrderedProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.OrderedProductService;
import com.warehouse.demo.util.EntityName;

@Service
public class OrderedProductServiceImpl extends AbstractService<OrderedProduct, Long> implements OrderedProductService {
    private final OrderedProductService self;

    private final OrderedProductRepository orderedProductRepository;

    private final OrderedProductRequestMapper orderedProductRequestMapper;

    public OrderedProductServiceImpl(@Lazy OrderedProductService self, OrderedProductRepository orderedProductRepository, OrderedProductRequestMapper orderedProductRequestMapper) {
        this.self = self;
        this.orderedProductRepository = orderedProductRepository;
        this.orderedProductRequestMapper = orderedProductRequestMapper;
    }

    @Override 
    @Cacheable(value = "orderedProducts", key = "#id")
    public OrderedProduct read(Long id) {
        return super.read(id);
    }

    @Override
    public OrderedProduct create(OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = new OrderedProduct();

        return modifyAndSave(orderedProduct, orderedProductRequest);
    }

    @Override
    @CacheEvict(value = "orderedProducts", key = "#id")
    public OrderedProduct update(long id, OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = self.read(id);

        return modifyAndSave(orderedProduct, orderedProductRequest);
    }

    @Override 
    @CacheEvict(value = "orderedProducts", key = "#id")
    public void delete(Long id) {
        super.delete(id);
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
