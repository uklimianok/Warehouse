package com.warehouse.demo.repository.workplace;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.workplace.Gate;

public interface GateRepository extends JpaRepository<Gate, Long> {
    boolean existsBySymbol(String symbol);
}
