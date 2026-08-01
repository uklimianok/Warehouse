package com.warehouse.demo.service;

import java.util.List;

import com.warehouse.demo.entity.employee.Position;

public interface PositionService {
    List<Position> findAll();
    Position findById(long id);
    Position save(Position position);
    void delete(long id);
}
