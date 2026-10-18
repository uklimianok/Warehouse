package local.warehouse.app.service.employee;

import local.warehouse.app.dto.employee.position.PositionRequest;
import local.warehouse.app.entity.employee.Position;
import local.warehouse.app.service.BaseService;

public interface PositionService extends BaseService<Position, Long> {
    Position create(PositionRequest positionRequest);
    Position update(long id, PositionRequest positionRequest);
}
