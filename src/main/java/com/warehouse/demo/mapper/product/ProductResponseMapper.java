package com.warehouse.demo.mapper.product;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.product.FullProductResponse;
import com.warehouse.demo.dto.product.ProductResponse;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.mapper.employee.organization.OrganizationResponseMapper;

@Mapper(componentModel = "spring", uses = OrganizationResponseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductResponseMapper {
    ProductResponse convertToResponse(Product product);
    FullProductResponse convertToFullResponse(Product product);
}
