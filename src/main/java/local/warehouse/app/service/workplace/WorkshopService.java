package local.warehouse.app.service.workplace;

import local.warehouse.app.dto.workplace.workshop.WorkshopRequest;
import local.warehouse.app.entity.workplace.Workshop;
import local.warehouse.app.service.BaseService;

public interface WorkshopService extends BaseService<Workshop, Long> {
    Workshop create(WorkshopRequest workshopRequest);
    Workshop update(long id, WorkshopRequest workshopRequest);
}
