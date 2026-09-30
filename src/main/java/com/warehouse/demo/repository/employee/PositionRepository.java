package com.warehouse.demo.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.warehouse.demo.entity.employee.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
    boolean existsByName(String name);
    Optional<Position> findByCodeName(String codeName);
    boolean existsByDepartmentId(long departmentId);
    @Query("SELECT p.inheritedPositions FROM Position p WHERE p.id = :id")
    List<Long> findInheritedPositionsIdById(long id);
    @Query(
        value = "SELECT code_name FROM position WHERE controller_access_flags ->> :key LIKE '%' || :value || '%'", 
        nativeQuery = true
    )   // "||" - SQL's concatenation, "%" - SQL's substring
    List<String> findCodeNameByControllerAccessFlagsContaining(@Param("key") String controllerName, @Param("value") String mode);
    List<Position> findAllByInheritedPositionsContainingCodeName(String codeName);
}
