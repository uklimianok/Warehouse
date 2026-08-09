package com.warehouse.demo.repository.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.service.ActionLog;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    boolean existsByEmployeeId(long id);
}
