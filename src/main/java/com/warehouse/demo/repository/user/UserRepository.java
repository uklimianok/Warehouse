package com.warehouse.demo.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.demo.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.employee e LEFT JOIN FETCH e.position WHERE e.employeeNumber = :employeeNumber") // evade LazyInitializationException
    Optional<User> findByEmployee_EmployeeNumber(String employeeNumber); 
    boolean existsByEmployeeId(long id);
}
