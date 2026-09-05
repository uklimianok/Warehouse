package com.warehouse.demo.mapper.item.pallet;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.item.pallet.PalletRequest;
import com.warehouse.demo.entity.item.Pallet;

@Mapper(componentModel = "spring")
public interface PalletRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(PalletRequest palletRequest, @MappingTarget Pallet pallet);
}
