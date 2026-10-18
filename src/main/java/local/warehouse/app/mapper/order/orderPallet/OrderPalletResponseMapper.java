package local.warehouse.app.mapper.order.orderPallet;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.order.orderPallet.FullOrderPalletResponse;
import local.warehouse.app.dto.order.orderPallet.OrderPalletResponse;
import local.warehouse.app.entity.order.OrderPallet;
import local.warehouse.app.mapper.item.pallet.PalletResponseMapper;
import local.warehouse.app.mapper.order.OrderResponseMapper;
import local.warehouse.app.mapper.service.status.StatusResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = {OrderResponseMapper.class, PalletResponseMapper.class, StatusResponseMapper.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface OrderPalletResponseMapper {
    OrderPalletResponse convertToResponse(OrderPallet orderPallet);
    FullOrderPalletResponse convertToFullResponse(OrderPallet orderPallet);
}
