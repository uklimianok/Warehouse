package com.warehouse.demo.repository.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.service.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
    boolean existsByNameAndType(String name, String type);
}
