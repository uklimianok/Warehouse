package local.warehouse.app.service.order;

import local.warehouse.app.dto.order.returnProduct.ReturnProductRequest;
import local.warehouse.app.entity.order.ReturnProduct;
import local.warehouse.app.service.BaseService;

public interface ReturnProductService extends BaseService<ReturnProduct, Long> {
    ReturnProduct create(ReturnProductRequest returnProductRequest);
    ReturnProduct update(long id, ReturnProductRequest returnProductRequest);
}
