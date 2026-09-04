package com.warehouse.demo.mapper.item.pallet;

import org.mapstruct.Mapper;

import com.warehouse.demo.dto.item.pallet.FullPalletResponse;
import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.entity.item.Pallet;

@Mapper(componentModel = "spring")
public interface PalletResponseMapper {
    PalletResponse convertToResponse(Pallet pallet);
    FullPalletResponse convertToFullResponse(Pallet pallet);
}
