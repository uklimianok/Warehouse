package com.warehouse.demo.service.product;

import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.service.BaseService;

public interface ProductService extends BaseService<Product, Long> {
    Product create(ProductRequest productRequest);
    Product update(long id, ProductRequest productRequest);
}
