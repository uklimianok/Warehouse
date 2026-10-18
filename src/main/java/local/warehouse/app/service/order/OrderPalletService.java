package local.warehouse.app.service.order;

import local.warehouse.app.dto.order.orderPallet.OrderPalletRequest;
import local.warehouse.app.entity.order.OrderPallet;
import local.warehouse.app.service.BaseService;

public interface OrderPalletService extends BaseService<OrderPallet, Long> {
    OrderPallet create(OrderPalletRequest orderPalletRequest);
    OrderPallet update(long id, OrderPalletRequest orderPalletRequest, String employeeNumber);
}
