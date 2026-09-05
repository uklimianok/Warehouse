package com.warehouse.demo.mapper.order.orderedProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.order.orderedProduct.OrderedProductRequest;
import com.warehouse.demo.entity.order.OrderedProduct;
import com.warehouse.demo.mapper.order.OrderResolver;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageResolver;

@Mapper(componentModel = "spring", uses = {OrderResolver.class, ProductPackageResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderedProductRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "orderedProductRequest.orderId")
    @Mapping(target = "productPackage", source = "orderedProductRequest.packageId")
    void convertFromRequest(OrderedProductRequest orderedProductRequest, @MappingTarget OrderedProduct orderedProduct);
}
