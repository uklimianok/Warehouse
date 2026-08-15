package com.warehouse.demo.service.product;

import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.service.BaseService;

public interface ProductPalletService extends BaseService<ProductPallet, Long> {
    ProductPallet create(ProductPalletRequest productPalletRequest);
    ProductPallet update(long id, ProductPalletRequest productPalletRequest);
}
