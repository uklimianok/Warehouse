package com.warehouse.demo.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.employee.OrganizationType;

public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, Long> {}
