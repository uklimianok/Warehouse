package com.warehouse.demo.repository.workplace;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.workplace.Workshop;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {}
