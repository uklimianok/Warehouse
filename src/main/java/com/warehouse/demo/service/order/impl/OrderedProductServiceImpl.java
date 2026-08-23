package com.warehouse.demo.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.orderedProduct.OrderedProductRequest;
import com.warehouse.demo.entity.order.OrderedProduct;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.order.OrderedProductRepository;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.OrderedProductService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderedProductServiceImpl extends AbstractService<OrderedProduct, Long> implements OrderedProductService {
    private final OrderedProductRepository orderedProductRepository;
    private final OrderRepository orderRepository;
    private final ProductPackageRepository productPackageRepository;

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
        target.setOrderedVolume(from.getOrderedVolume());

        target.setOrder(
            orderRepository.findById(from.getOrderId())
                .orElseThrow(() -> 
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.ORDER, OutputMessage.NOT_FOUND))
                )
        );
        target.setProductPackage(
            productPackageRepository.findById(from.getPackageId())
                .orElseThrow(() -> 
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.PRODUCT_PACKAGE, OutputMessage.NOT_FOUND))
                )
        );

        return orderedProductRepository.save(target);
    }
}
