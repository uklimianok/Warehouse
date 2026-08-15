package com.warehouse.demo.service.employee.impl;

import org.springframework.dao.DataIntegrityViolationException;
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
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

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
        if (shiftRepository.existsBySymbol(shiftRequest.getSymbol()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        Shift shift = new Shift();
        
        return modifyAndSave(shift, shiftRequest);
    }

    @Override
    public Shift update(long id, ShiftRequest shiftRequest) {
        Shift shift = read(id);
        boolean shiftChanged = !shift.getSymbol().equals(shiftRequest.getSymbol());
        boolean shiftExists = shiftRepository.existsBySymbol(shiftRequest.getSymbol());
        if (shiftChanged && shiftExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(shift, shiftRequest);
    }

    private Shift modifyAndSave(Shift shift, ShiftRequest shiftRequest) {
        shift.setSymbol(shiftRequest.getSymbol());
        return shiftRepository.save(shift);
    }
}
