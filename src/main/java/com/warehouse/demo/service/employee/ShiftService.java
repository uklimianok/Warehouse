package com.warehouse.demo.service.employee;

import com.warehouse.demo.dto.employee.shift.ShiftRequest;
import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.service.BaseService;

public interface ShiftService extends BaseService<Shift, Long> {
    Shift create(ShiftRequest shiftRequest);
    Shift update(long id, ShiftRequest shiftRequest);
}
