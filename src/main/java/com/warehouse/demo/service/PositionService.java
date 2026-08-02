package com.warehouse.demo.service;

import java.util.List;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.entity.employee.Position;

public interface PositionService {
    List<Position> readAll();
    Position readById(long id);
    Position create(PositionRequest positionRequest);
    Position update(long id, PositionRequest positionRequest);
    void delete(long id);
}
