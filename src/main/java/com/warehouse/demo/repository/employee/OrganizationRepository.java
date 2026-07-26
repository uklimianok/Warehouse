package com.warehouse.demo.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.employee.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {}
