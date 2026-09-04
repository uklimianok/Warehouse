package com.warehouse.demo.mapper.order.orderPallet;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.order.orderPallet.FullOrderPalletResponse;
import com.warehouse.demo.dto.order.orderPallet.OrderPalletResponse;
import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.mapper.item.pallet.PalletResponseMapper;
import com.warehouse.demo.mapper.order.OrderResponseMapper;
import com.warehouse.demo.mapper.service.status.StatusResponseMapper;

@Mapper(componentModel = "spring", uses = {OrderResponseMapper.class, PalletResponseMapper.class, StatusResponseMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderPalletResponseMapper {
    OrderPalletResponse convertToResponse(OrderPallet orderPallet);
    FullOrderPalletResponse convertToFullResponse(OrderPallet orderPallet);
}
