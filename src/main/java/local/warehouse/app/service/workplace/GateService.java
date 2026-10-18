package local.warehouse.app.service.workplace;

import local.warehouse.app.dto.workplace.gate.GateRequest;
import local.warehouse.app.entity.workplace.Gate;
import local.warehouse.app.service.BaseService;

public interface GateService extends BaseService<Gate, Long> {
    Gate create(GateRequest gateRequest);
    Gate update(long id, GateRequest gateRequest);
}
