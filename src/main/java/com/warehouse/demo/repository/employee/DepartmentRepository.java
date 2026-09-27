package com.warehouse.demo.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.employee.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);
}
