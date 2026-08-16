package com.warehouse.demo.service.order;

import com.warehouse.demo.dto.order.returnProduct.ReturnProductRequest;
import com.warehouse.demo.entity.order.ReturnProduct;
import com.warehouse.demo.service.BaseService;

public interface ReturnProductService extends BaseService<ReturnProduct, Long> {
    ReturnProduct create(ReturnProductRequest returnProductRequest);
    ReturnProduct update(long id, ReturnProductRequest returnProductRequest);
}
