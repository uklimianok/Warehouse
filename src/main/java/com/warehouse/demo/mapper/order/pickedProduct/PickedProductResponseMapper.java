package com.warehouse.demo.mapper.order.pickedProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.order.pickedProduct.PickedProductResponse;
import com.warehouse.demo.entity.order.PickedProduct;
import com.warehouse.demo.mapper.order.orderPallet.OrderPalletResponseMapper;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageResponseMapper;

@Mapper(componentModel = "spring", uses = {OrderPalletResponseMapper.class, ProductPackageResponseMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PickedProductResponseMapper {
    PickedProductResponse convertToResponse(PickedProduct pickedProduct);
}
