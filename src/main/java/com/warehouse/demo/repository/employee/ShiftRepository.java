package com.warehouse.demo.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.employee.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    boolean existsBySymbol(String symbol);
}
