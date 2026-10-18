package local.warehouse.app.service.warehouseService.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import local.warehouse.app.entity.service.ActionLog;
import local.warehouse.app.event.service.ActionLogEvent;
import local.warehouse.app.repository.service.ActionLogRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.warehouseService.ActionLogService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl extends AbstractService<ActionLog, Long> implements ActionLogService {
    private final ActionLogRepository actionLogRepository;

    private static final String ACTION_LOG_DELETE_RESTRICTED = "cannot be deleted.";

    @KafkaListener(groupId = "1", topics = "action-log-event")
    public ActionLog consumeEvent(ActionLogEvent actionLogEvent) {
        return log(actionLogEvent);
    }

    @Override
    public ActionLog log(ActionLogEvent actionLogEvent) {
        ActionLog actionLog = new ActionLog();
        actionLog.setEmployeeNumber(actionLogEvent.getEmployeeNumber());
        actionLog.setProceededAt(actionLogEvent.getProceededAt());
        actionLog.setEntityType(actionLogEvent.getEntityType());
        actionLog.setEntityId(actionLogEvent.getEntityId());
        actionLog.setAction(actionLogEvent.getAction());

        return actionLogRepository.save(actionLog);
    }

    @Override
    public void delete(Long id) {
        throw new BusinessRuleException(MessageHandler.getOutputMessage(Entity.ACTION_LOG, ACTION_LOG_DELETE_RESTRICTED));
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
