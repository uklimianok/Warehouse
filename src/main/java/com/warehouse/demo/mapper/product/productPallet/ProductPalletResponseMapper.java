package com.warehouse.demo.mapper.product.productPallet;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.product.productPallet.FullProductPalletResponse;
import com.warehouse.demo.dto.product.productPallet.ProductPalletResponse;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.mapper.item.pallet.PalletResponseMapper;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageResponseMapper;
import com.warehouse.demo.mapper.service.status.StatusResponseMapper;
import com.warehouse.demo.mapper.workplace.workStation.WorkStationResponseMapper;

@Mapper(componentModel = "spring", uses = {ProductPackageResponseMapper.class, PalletResponseMapper.class, StatusResponseMapper.class, WorkStationResponseMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductPalletResponseMapper {
    ProductPalletResponse convertToResponse(ProductPallet productPallet);
    FullProductPalletResponse convertToFullResponse(ProductPallet productPallet);
}
