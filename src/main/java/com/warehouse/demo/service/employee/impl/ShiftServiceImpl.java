package com.warehouse.demo.service.employee.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.shift.ShiftRequest;
import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.ShiftRepository;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.ShiftService;
import com.warehouse.demo.util.EntityName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl extends AbstractService<Shift, Long> implements ShiftService {
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;

    @Override
    protected JpaRepository<Shift, Long> getRepository() {
        return shiftRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.SHIFT;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInEmployee = employeeRepository.existsByShiftId(id);
        boolean activeInOrder = orderRepository.existsByShiftId(id);
        return activeInEmployee || activeInOrder;
    }

    @Override
    public Shift create(ShiftRequest shiftRequest) {
        return modifyAndSave(new Shift(), shiftRequest);
    }

    @Override
    public Shift update(long id, ShiftRequest shiftRequest) {
        return modifyAndSave(read(id), shiftRequest);
    }

    private Shift modifyAndSave(Shift shift, ShiftRequest shiftRequest) {
        shift.setSymbol(shiftRequest.getSymbol());
        return shiftRepository.save(shift);
    }
}
