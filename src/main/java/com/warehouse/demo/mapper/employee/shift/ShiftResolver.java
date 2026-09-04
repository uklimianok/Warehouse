package com.warehouse.demo.mapper.employee.shift;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.repository.employee.ShiftRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShiftResolver {
    private final ShiftRepository shiftRepository;

    public Shift mapShift(long shiftId) {
        return shiftRepository.findById(shiftId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.SHIFT, OutputMessage.NOT_FOUND)));
    }
}
