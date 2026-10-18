package local.warehouse.app.service.order;

import local.warehouse.app.dto.order.orderedProduct.OrderedProductRequest;
import local.warehouse.app.entity.order.OrderedProduct;
import local.warehouse.app.service.BaseService;

public interface OrderedProductService extends BaseService<OrderedProduct, Long> {
    OrderedProduct create(OrderedProductRequest orderedProductRequest);
    OrderedProduct update(long id, OrderedProductRequest orderedProductRequest);
}
