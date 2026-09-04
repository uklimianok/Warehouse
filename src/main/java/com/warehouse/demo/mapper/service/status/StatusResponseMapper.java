package com.warehouse.demo.mapper.service.status;

import org.mapstruct.Mapper;

import com.warehouse.demo.dto.service.status.StatusResponse;
import com.warehouse.demo.entity.service.Status;

@Mapper(componentModel = "spring")
public interface StatusResponseMapper {
    StatusResponse convertToResponse(Status status);
}
