package local.warehouse.app.service.workplace;

import local.warehouse.app.dto.workplace.workStation.WorkStationRequest;
import local.warehouse.app.entity.workplace.WorkStation;
import local.warehouse.app.service.BaseService;

public interface WorkStationService extends BaseService<WorkStation, Long> {
    WorkStation create(WorkStationRequest workStationRequest);
    WorkStation update(long id, WorkStationRequest workStationRequest);
}
