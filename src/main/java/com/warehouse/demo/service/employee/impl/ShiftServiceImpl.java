package com.warehouse.demo.service.employee.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.shift.ShiftRequest;
import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.ShiftRepository;
import com.warehouse.demo.service.AbstractCrudService;
import com.warehouse.demo.service.employee.ShiftService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl extends AbstractCrudService<Shift, Long> implements ShiftService {
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    protected JpaRepository<Shift, Long> getRepository() {
        return shiftRepository;
    }

    @Override
    protected String getEntityName() {
        return "Shift";
    }

    @Override
    protected boolean isUsed(Long id) {
        return employeeRepository.existsByShiftId(id);
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
