package com.warehouse.demo.mapper.employee.shift;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.repository.employee.ShiftRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShiftResolver {
    private final ShiftRepository shiftRepository;

    public Shift mapShift(long shiftId) {
        return shiftRepository.findById(shiftId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(Entity.SHIFT, OutputMessage.NOT_FOUND)));
    }
}
