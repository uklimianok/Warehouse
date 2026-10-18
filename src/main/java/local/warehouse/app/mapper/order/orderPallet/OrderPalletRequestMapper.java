package local.warehouse.app.mapper.order.orderPallet;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.order.orderPallet.OrderPalletRequest;
import local.warehouse.app.entity.order.OrderPallet;
import local.warehouse.app.mapper.item.pallet.PalletResolver;
import local.warehouse.app.mapper.order.OrderResolver;
import local.warehouse.app.mapper.service.status.StatusResolver;

@Mapper(
    componentModel = "spring", 
    uses = {OrderResolver.class, PalletResolver.class, StatusResolver.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
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
