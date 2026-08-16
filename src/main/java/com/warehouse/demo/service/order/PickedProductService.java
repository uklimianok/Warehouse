package com.warehouse.demo.service.order;

import com.warehouse.demo.dto.order.pickedProduct.PickedProductRequest;
import com.warehouse.demo.entity.order.PickedProduct;
import com.warehouse.demo.service.BaseService;

public interface PickedProductService extends BaseService<PickedProduct, Long> {
    PickedProduct create(PickedProductRequest pickedProductRequest);
    PickedProduct update(long id, PickedProductRequest pickedProductRequest);
}
