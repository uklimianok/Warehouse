package com.warehouse.demo.mapper.order.pickedProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.order.pickedProduct.PickedProductRequest;
import com.warehouse.demo.entity.order.PickedProduct;
import com.warehouse.demo.mapper.order.orderPallet.OrderPalletResolver;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageResolver;

@Mapper(componentModel = "spring", uses = {OrderPalletResolver.class, ProductPackageResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PickedProductRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderPallet", source = "pickedProductRequest.orderPalletId")
    @Mapping(target = "productPackage", source = "pickedProductRequest.packageId")
    PickedProduct convertFromRequest(PickedProductRequest pickedProductRequest, @MappingTarget PickedProduct pickedProduct);
}
