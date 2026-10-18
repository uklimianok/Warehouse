package local.warehouse.app.mapper.order;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.order.OrderRequest;
import local.warehouse.app.entity.order.Order;
import local.warehouse.app.mapper.employee.organization.OrganizationResolver;
import local.warehouse.app.mapper.employee.shift.ShiftResolver;
import local.warehouse.app.mapper.service.status.StatusResolver;
import local.warehouse.app.mapper.workplace.gate.GateResolver;

@Mapper(
    componentModel = "spring", 
    uses = {OrganizationResolver.class, GateResolver.class, ShiftResolver.class, StatusResolver.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface OrderRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", source = "orderRequest.storeId")
    @Mapping(target = "gate", source = "orderRequest.gateId")
    @Mapping(target = "shift", source = "orderRequest.shiftId")
    @Mapping(target = "status", ignore = true)
    void convertFromRequest(OrderRequest orderRequest, @MappingTarget Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "gate", ignore = true)
    @Mapping(target = "shift", ignore = true)
    @Mapping(target = "status", ignore = true)
    void convertFromWarehouseEmployeeRequest(OrderRequest orderRequest, @MappingTarget Order order);
}
