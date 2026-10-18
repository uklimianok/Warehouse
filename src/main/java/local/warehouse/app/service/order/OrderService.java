package local.warehouse.app.service.order;

import local.warehouse.app.dto.order.OrderRequest;
import local.warehouse.app.entity.order.Order;
import local.warehouse.app.service.BaseService;

public interface OrderService extends BaseService<Order, Long> {
    Order create(OrderRequest orderRequest);
    Order update(long id, OrderRequest orderRequest, String employeeNumber);
}
