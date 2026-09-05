package com.warehouse.demo.mapper.product.productPallet;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.mapper.item.pallet.PalletResolver;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageResolver;
import com.warehouse.demo.mapper.service.status.StatusResolver;
import com.warehouse.demo.mapper.workplace.workStation.WorkStationResolver;

@Mapper(componentModel = "spring", uses = {ProductPackageResolver.class, PalletResolver.class, StatusResolver.class, WorkStationResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductPalletRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productPackage", source = "productPalletRequest.productPackageId")
    @Mapping(target = "pallet", source = "productPalletRequest.palletId")
    @Mapping(target = "status", source = "productPalletRequest.statusId")
    @Mapping(target = "workStation", source = "productPalletRequest.workStationId")
    @Mapping(target = "nextWorkStation", source = "productPalletRequest.nextWorkStationId")
    void convertFromRequest(ProductPalletRequest productPalletRequest, @MappingTarget ProductPallet productPallet);
}
