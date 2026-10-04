package com.warehouse.demo.util.action;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.repository.employee.PositionRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class PositionInheritanceTree {
    private final PositionRepository positionRepository;

    public boolean isOneOrDescendant(Position subject, String ascendantCode) {
        return positionRepository.codeNameIsTargetOrDescendant(ascendantCode, subject.getId());
    }
}
