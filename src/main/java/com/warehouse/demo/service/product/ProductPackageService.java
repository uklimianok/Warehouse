package com.warehouse.demo.service.product;

import com.warehouse.demo.dto.product.productPackage.ProductPackageRequest;
import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.service.BaseService;

public interface ProductPackageService extends BaseService<ProductPackage, Long> {
    ProductPackage create(ProductPackageRequest productPackageRequest);
    ProductPackage update(long id, ProductPackageRequest productPackageRequest);
}
