package com.warehouse.demo.mapper.order.returnProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.order.returnProduct.ReturnProductRequest;
import com.warehouse.demo.entity.order.ReturnProduct;
import com.warehouse.demo.mapper.order.OrderResolver;
import com.warehouse.demo.mapper.product.ProductResolver;

@Mapper(componentModel = "spring", uses = {OrderResolver.class, ProductResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ReturnProductRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "returnProductRequest.orderId")
    @Mapping(target = "product", source = "returnProductRequest.productId")
    void convertFromRequest(ReturnProductRequest returnProductRequest, @MappingTarget ReturnProduct returnProduct);
}
