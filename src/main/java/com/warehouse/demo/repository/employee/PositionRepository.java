package com.warehouse.demo.repository.employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.employee.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
    boolean existsByName(String name);
    Optional<Position> findByCodeName(String codeName);
}
