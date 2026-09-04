package com.warehouse.demo.mapper.service.actionLog;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.service.actionLog.ActionLogResponse;
import com.warehouse.demo.entity.service.ActionLog;
import com.warehouse.demo.mapper.employee.EmployeeResponseMapper;

@Mapper(componentModel = "spring", uses = EmployeeResponseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ActionLogResponseMapper {
    ActionLogResponse convertToResponse(ActionLog actionLog);
}
