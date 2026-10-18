package local.warehouse.app.service.warehouseService;

import local.warehouse.app.entity.service.ActionLog;
import local.warehouse.app.event.service.ActionLogEvent;
import local.warehouse.app.service.BaseService;

public interface ActionLogService extends BaseService<ActionLog, Long> {
    ActionLog log(ActionLogEvent actionLogEvent);
}
