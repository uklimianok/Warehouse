package com.warehouse.demo.service.order;

import com.warehouse.demo.dto.order.orderedProduct.OrderedProductRequest;
import com.warehouse.demo.entity.order.OrderedProduct;
import com.warehouse.demo.service.BaseService;

public interface OrderedProductService extends BaseService<OrderedProduct, Long> {
    OrderedProduct create(OrderedProductRequest orderedProductRequest);
    OrderedProduct update(long id, OrderedProductRequest orderedProductRequest);
}
