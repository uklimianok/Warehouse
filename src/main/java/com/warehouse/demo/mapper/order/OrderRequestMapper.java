package com.warehouse.demo.mapper.order;

import org.mapstruct.Builder;
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

@Mapper(componentModel = "spring", uses = {OrganizationResolver.class, GateResolver.class, ShiftResolver.class, StatusResolver.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, builder = @Builder(disableBuilder = true))
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
