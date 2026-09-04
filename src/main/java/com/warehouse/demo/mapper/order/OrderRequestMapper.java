package com.warehouse.demo.mapper.order;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.order.OrderRequest;
import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.mapper.employee.organization.OrganizationResolver;
import com.warehouse.demo.mapper.employee.shift.ShiftResolver;
import com.warehouse.demo.mapper.service.status.StatusResolver;
import com.warehouse.demo.mapper.workplace.gate.GateResolver;

@Mapper(componentModel = "spring", uses = {OrganizationResolver.class, GateResolver.class, ShiftResolver.class, StatusResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", source = "orderRequest.storeId")
    @Mapping(target = "gate", source = "orderRequest.gateId")
    @Mapping(target = "shift", source = "orderRequest.shiftId")
    @Mapping(target = "status", source = "orderRequest.statusId")
    Order convertFromRequest(OrderRequest orderRequest, @MappingTarget Order order);
}
