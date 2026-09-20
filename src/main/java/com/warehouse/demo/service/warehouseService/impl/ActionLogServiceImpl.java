package com.warehouse.demo.service.warehouseService.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.warehouse.demo.entity.service.ActionLog;
import com.warehouse.demo.event.service.ActionLogEvent;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.service.ActionLogRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.warehouseService.ActionLogService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl extends AbstractService<ActionLog, Long> implements ActionLogService {
    private final ActionLogRepository actionLogRepository;
    private final EmployeeRepository employeeRepository;

    private static final String ACTION_LOG_DELETE_RESTRICTED = "cannot be deleted.";

    @KafkaListener(groupId = "1", topics = "action-log-event")
    public ActionLog consumeEvent(ActionLogEvent actionLogEvent) {
        return log(actionLogEvent);
    }

    @Override
    public ActionLog log(ActionLogEvent actionLogEvent) {
        ActionLog actionLog = new ActionLog();
        actionLog.setEmployee(employeeRepository.findById(actionLogEvent.getEmployeeId())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND))));
        actionLog.setProceededAt(actionLogEvent.getProceededAt());
        actionLog.setEntityType(actionLogEvent.getEntityType());
        actionLog.setEntityId(actionLogEvent.getEntityId());
        actionLog.setAction(actionLogEvent.getAction());

        return actionLogRepository.save(actionLog);
    }

    @Override
    public void delete(Long id) {
        throw new DataIntegrityViolationException(Utility.getOutputMessage(Entity.ACTION_LOG, ACTION_LOG_DELETE_RESTRICTED));
    }

    @Override
    protected JpaRepository<ActionLog, Long> getRepository() {
        return actionLogRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.ACTION_LOG;
    }
}
