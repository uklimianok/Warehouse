package com.warehouse.demo.service.workplace;

import com.warehouse.demo.dto.workplace.gate.GateRequest;
import com.warehouse.demo.entity.workplace.Gate;
import com.warehouse.demo.service.BaseService;

public interface GateService extends BaseService<Gate, Long> {
    Gate create(GateRequest gateRequest);
    Gate update(long id, GateRequest gateRequest);
}
