package com.warehouse.demo.service.employee;

import com.warehouse.demo.dto.employee.shift.ShiftRequest;
import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.service.AbstractService;

public interface ShiftService extends AbstractService<Shift, Long> {
    Shift create(ShiftRequest shiftRequest);
    Shift update(long id, ShiftRequest shiftRequest);
}
