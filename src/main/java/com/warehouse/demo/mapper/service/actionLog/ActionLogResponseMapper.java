package com.warehouse.demo.mapper.service.actionLog;

import org.mapstruct.Mapper;

import com.warehouse.demo.dto.service.actionLog.ActionLogResponse;
import com.warehouse.demo.entity.service.ActionLog;

@Mapper(componentModel = "spring")
public interface ActionLogResponseMapper {
    ActionLogResponse convertToResponse(ActionLog actionLog);
}
