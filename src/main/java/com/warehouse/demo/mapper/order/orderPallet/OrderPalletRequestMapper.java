package com.warehouse.demo.mapper.order.orderPallet;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.order.orderPallet.OrderPalletRequest;
import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.mapper.item.pallet.PalletResolver;
import com.warehouse.demo.mapper.order.OrderResolver;
import com.warehouse.demo.mapper.service.status.StatusResolver;

@Mapper(componentModel = "spring", uses = {OrderResolver.class, PalletResolver.class, StatusResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, builder = @Builder(disableBuilder = true))
public interface OrderPalletRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "orderPalletRequest.orderId")
    @Mapping(target = "pallet", source = "orderPalletRequest.palletId")
    @Mapping(target = "status", ignore = true)
    void convertFromRequest(OrderPalletRequest orderPalletRequest, @MappingTarget OrderPallet orderPallet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "pallet", ignore = true)
    @Mapping(target = "status", ignore = true)
    void convertFromWarehouseEmployeeRequest(OrderPalletRequest orderPalletRequest, @MappingTarget OrderPallet orderPallet);
}
