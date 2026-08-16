package com.warehouse.demo.service.warehouseService;

import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.service.ActionLog;
import com.warehouse.demo.service.BaseService;

public interface ActionLogService extends BaseService<ActionLog, Long> {
    ActionLog log(Employee employee, String entityType, long entityId, String action);
}
