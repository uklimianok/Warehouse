package com.warehouse.demo.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // SELECT u FROM User u LEFT JOIN u.employee e WHERE e.employeeNumber = :employeeNumber
    Optional<User> findByEmployee_EmployeeNumber(String employeeNumber); 
}
