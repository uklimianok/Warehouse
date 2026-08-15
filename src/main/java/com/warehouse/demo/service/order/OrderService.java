package com.warehouse.demo.service.order;

import com.warehouse.demo.dto.order.OrderRequest;
import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.service.BaseService;

public interface OrderService extends BaseService<Order, Long> {
    Order create(OrderRequest orderRequest);
    Order update(long id, OrderRequest orderRequest);
}
