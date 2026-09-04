package com.warehouse.demo.mapper.employee.position;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionResolver {
    private final PositionRepository positionRepository;

    public Position mapPosition(long positionId) {
        return positionRepository.findById(positionId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.POSITION, OutputMessage.NOT_FOUND)));
    }
}
