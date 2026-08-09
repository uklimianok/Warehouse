package com.warehouse.demo.service.warehouseService;

import com.warehouse.demo.dto.service.status.StatusRequest;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.service.BaseService;

public interface StatusService extends BaseService<Status, Long> {
    Status create(StatusRequest statusRequest);
    Status update(long id, StatusRequest statusRequest);
}
