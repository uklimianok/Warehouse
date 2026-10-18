package local.warehouse.app.service.warehouseService;

import local.warehouse.app.dto.service.status.StatusRequest;
import local.warehouse.app.entity.service.Status;
import local.warehouse.app.service.BaseService;

public interface StatusService extends BaseService<Status, Long> {
    Status readByNameAndType(String name, String type);
    Status create(StatusRequest statusRequest);
    Status update(long id, StatusRequest statusRequest);
}
