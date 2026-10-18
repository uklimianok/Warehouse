package local.warehouse.app.service.order;

import local.warehouse.app.dto.order.pickedProduct.PickedProductRequest;
import local.warehouse.app.entity.order.PickedProduct;
import local.warehouse.app.service.BaseService;

public interface PickedProductService extends BaseService<PickedProduct, Long> {
    PickedProduct create(PickedProductRequest pickedProductRequest);
    PickedProduct update(long id, PickedProductRequest pickedProductRequest, String employeeNumber);
}
