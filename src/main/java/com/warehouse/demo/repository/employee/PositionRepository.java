package com.warehouse.demo.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.employee.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {}
