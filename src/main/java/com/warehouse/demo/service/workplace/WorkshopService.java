package com.warehouse.demo.service.workplace;

import com.warehouse.demo.dto.workplace.workshop.WorkshopRequest;
import com.warehouse.demo.entity.workplace.Workshop;
import com.warehouse.demo.service.BaseService;

public interface WorkshopService extends BaseService<Workshop, Long> {
    Workshop create(WorkshopRequest workshopRequest);
    Workshop update(long id, WorkshopRequest workshopRequest);
}
