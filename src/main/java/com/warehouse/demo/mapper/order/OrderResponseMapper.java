package com.warehouse.demo.mapper.order;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.order.FullOrderResponse;
import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.mapper.employee.organization.OrganizationResponseMapper;
import com.warehouse.demo.mapper.employee.shift.ShiftResponseMapper;
import com.warehouse.demo.mapper.service.status.StatusResponseMapper;
import com.warehouse.demo.mapper.workplace.gate.GateResponseMapper;

@Mapper(componentModel = "spring", uses = {OrganizationResponseMapper.class, GateResponseMapper.class, ShiftResponseMapper.class, StatusResponseMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderResponseMapper {
    OrderResponse convertToResponse(Order order);
    FullOrderResponse convertToFullResponse(Order order);
}
