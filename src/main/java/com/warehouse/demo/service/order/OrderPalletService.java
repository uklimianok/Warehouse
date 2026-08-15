package com.warehouse.demo.service.order;

import com.warehouse.demo.dto.order.orderPallet.OrderPalletRequest;
import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.service.BaseService;

public interface OrderPalletService extends BaseService<OrderPallet, Long> {
    OrderPallet create(OrderPalletRequest orderPalletRequest);
    OrderPallet update(long id, OrderPalletRequest orderPalletRequest);
}
