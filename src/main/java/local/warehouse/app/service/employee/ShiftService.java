package local.warehouse.app.service.employee;

import local.warehouse.app.dto.employee.shift.ShiftRequest;
import local.warehouse.app.entity.employee.Shift;
import local.warehouse.app.service.BaseService;

public interface ShiftService extends BaseService<Shift, Long> {
    Shift create(ShiftRequest shiftRequest);
    Shift update(long id, ShiftRequest shiftRequest);
}
