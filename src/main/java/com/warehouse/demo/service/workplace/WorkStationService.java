package com.warehouse.demo.service.workplace;

import com.warehouse.demo.dto.workplace.workStation.WorkStationRequest;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.service.BaseService;

public interface WorkStationService extends BaseService<WorkStation, Long> {
    WorkStation create(WorkStationRequest workStationRequest);
    WorkStation update(long id, WorkStationRequest workStationRequest);
}
