package com.warehouse.demo.mapper.product.productPackage;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.product.productPackage.ProductPackageRequest;
import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.mapper.product.ProductResolver;

@Mapper(componentModel = "spring", uses = ProductResolver.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductPackageRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "productPackageRequest.productId")
    void convertFromRequest(ProductPackageRequest productPackageRequest, @MappingTarget ProductPackage productPackage);
}
