package com.warehouse.demo.mapper.product.productPackage;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;
import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.mapper.product.ProductResponseMapper;

@Mapper(componentModel = "spring", uses = ProductResponseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR, builder = @Builder(disableBuilder = true))
public interface ProductPackageResponseMapper {
    ProductPackageResponse convertToResponse(ProductPackage productPackage);
}
