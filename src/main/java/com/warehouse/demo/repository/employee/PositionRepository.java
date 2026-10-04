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

    @Query("SELECT ip.id FROM Position p JOIN p.inheritedPositions ip WHERE p.id = :id")
    List<Long> findInheritedPositionsIdById(long id);

    @Query(
        value = """
        SELECT EXISTS (
            SELECT 1 
            FROM employees e 
            JOIN positions p ON p.id = e.position_id 
            WHERE e.employee_number = :employeeNumber 
                AND strpos(p.controller_flags -> :controllerName ->> 0, :mode) > 0
        )
        """, 
        nativeQuery = true
    )
    boolean modeIsContainedInControllerFlagsByEmployeeNumber(String controllerName, String employeeNumber, String mode);

    @Query(
        value = """
            WITH RECURSIVE descendants AS (
                SELECT p.id
                FROM positions p 
                WHERE p.code_name = :parentCodeName 
                UNION 
                SELECT pi.position_id
                FROM descendants d 
                JOIN position_inheritances pi ON pi.inherited_position_id = d.id
            )
            SELECT EXISTS (SELECT 1 FROM descendants WHERE id = :employeeId)
        """,
        nativeQuery = true
    )
    boolean codeNameIsTargetOrDescendant(String parentCodeName, long employeeId);
    
    @Query(
        value = "SELECT (controller_flags -> :key ->> 1) FROM positions WHERE id = (SELECT position_id FROM employees WHERE employee_number = :number)",
        nativeQuery = true
    )
    Optional<String> findControllerFlags_ResponseObjectTypeByEmployee_EmployeeNumberAndControllerFlagsEquals(@Param("key") String controllerName, @Param("number") String employeeNumber);
}
