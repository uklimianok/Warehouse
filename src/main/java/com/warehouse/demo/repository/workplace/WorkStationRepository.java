package com.warehouse.demo.repository.workplace;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.workplace.WorkStation;

public interface WorkStationRepository extends JpaRepository<WorkStation, Long> {}
