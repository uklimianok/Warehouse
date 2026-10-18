package local.warehouse.app.repository.service;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.service.ActionLog;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    
    boolean existsByEmployeeNumber(String employeeNumber);
}
