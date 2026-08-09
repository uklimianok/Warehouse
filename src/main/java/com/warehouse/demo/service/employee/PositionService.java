package com.warehouse.demo.service.employee;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.service.BaseService;

public interface PositionService extends BaseService<Position, Long> {
    Position create(PositionRequest positionRequest);
    Position update(long id, PositionRequest positionRequest);
}
