package com.warehouse.demo.mapper.order.orderedProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.order.orderedProduct.OrderedProductResponse;
import com.warehouse.demo.entity.order.OrderedProduct;
import com.warehouse.demo.mapper.order.OrderResponseMapper;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageResponseMapper;

@Mapper(componentModel = "spring", uses = {OrderResponseMapper.class, ProductPackageResponseMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderedProductResponseMapper {
    OrderedProductResponse convertToResponse(OrderedProduct orderedProduct);
}
