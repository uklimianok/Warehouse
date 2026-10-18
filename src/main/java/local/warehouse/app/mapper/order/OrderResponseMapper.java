package local.warehouse.app.mapper.order;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.order.FullOrderResponse;
import local.warehouse.app.dto.order.OrderResponse;
import local.warehouse.app.entity.order.Order;
import local.warehouse.app.mapper.employee.organization.OrganizationResponseMapper;
import local.warehouse.app.mapper.employee.shift.ShiftResponseMapper;
import local.warehouse.app.mapper.service.status.StatusResponseMapper;
import local.warehouse.app.mapper.workplace.gate.GateResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = {OrganizationResponseMapper.class, GateResponseMapper.class, ShiftResponseMapper.class, StatusResponseMapper.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface OrderResponseMapper {
    OrderResponse convertToResponse(Order order);
    FullOrderResponse convertToFullResponse(Order order);
}
