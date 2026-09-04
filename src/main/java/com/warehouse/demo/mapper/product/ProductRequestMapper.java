package com.warehouse.demo.mapper.product;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.mapper.employee.organization.OrganizationResolver;

@Mapper(componentModel = "spring", uses = OrganizationResolver.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producer", source = "productRequest.producerId")
    void convertFromRequest(ProductRequest productRequest, @MappingTarget Product product);
}