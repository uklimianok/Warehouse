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
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl extends AbstractService<ActionLog, Long> implements ActionLogService {
    private final ActionLogRepository actionLogRepository;
    private final EmployeeRepository employeeRepository;

    private static final String ACTION_LOG_DELETE_RESTRICTED = "cannot be deleted.";

    @KafkaListener(groupId = "1", topics = "action-log-events")
    public ActionLog listen(ActionLogEvent actionLogEvent) {
        return log(actionLogEvent);
    }

    @Override
    public ActionLog log(ActionLogEvent actionLogEvent) {
        ActionLog actionLog = new ActionLog();
        actionLog.setEmployee(employeeRepository.findById(actionLogEvent.getEmployeeId())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.EMPLOYEE, OutputMessage.NOT_FOUND))));
        actionLog.setProceededAt(actionLogEvent.getProceededAt());
        actionLog.setEntityType(actionLogEvent.getEntityType());
        actionLog.setEntityId(actionLogEvent.getEntityId());
        actionLog.setAction(actionLogEvent.getAction());

        return actionLogRepository.save(actionLog);
    }

    @Override
    public void delete(Long id) {
        throw new DataIntegrityViolationException(Utility.getOutputMessage(EntityName.ACTION_LOG, ACTION_LOG_DELETE_RESTRICTED));
    }

    @Override
    protected JpaRepository<ActionLog, Long> getRepository() {
        return actionLogRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.ACTION_LOG;
    }
}
