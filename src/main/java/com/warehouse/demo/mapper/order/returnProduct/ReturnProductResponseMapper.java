package com.warehouse.demo.mapper.order.returnProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.order.returnProduct.ReturnProductResponse;
import com.warehouse.demo.entity.order.ReturnProduct;
import com.warehouse.demo.mapper.order.OrderResponseMapper;
import com.warehouse.demo.mapper.product.ProductResponseMapper;

@Mapper(componentModel = "spring", uses = {OrderResponseMapper.class, ProductResponseMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ReturnProductResponseMapper {
    ReturnProductResponse convertToResponse(ReturnProduct returnProduct);
}
