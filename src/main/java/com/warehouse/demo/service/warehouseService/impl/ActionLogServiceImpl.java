package com.warehouse.demo.service.warehouseService.impl;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.service.ActionLog;
import com.warehouse.demo.repository.service.ActionLogRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.warehouseService.ActionLogService;
import com.warehouse.demo.util.EntityName;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl extends AbstractService<ActionLog, Long> implements ActionLogService {
    private final ActionLogRepository actionLogRepository;

    @Override
    public ActionLog log(Employee employee, String entityType, long entityId, String action) {
        ActionLog actionLog = new ActionLog();
        actionLog.setEmployee(employee);
        actionLog.setProceededAt(LocalDateTime.now());
        actionLog.setEntityType(entityType);
        actionLog.setEntityId(entityId);
        actionLog.setAction(action);

        return actionLogRepository.save(actionLog);
    }

    @Override
    public void delete(Long id) {}  // prevent from deleting

    @Override
    protected JpaRepository<ActionLog, Long> getRepository() {
        return actionLogRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.ACTION_LOG;
    }

    @Override
    protected boolean isUsed(Long id) {
        return false;
    }
}
