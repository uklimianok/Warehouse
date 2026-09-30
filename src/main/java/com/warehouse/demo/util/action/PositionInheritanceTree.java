package com.warehouse.demo.util.action;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.repository.employee.PositionRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class PositionInheritanceTree {
    private final PositionRepository positionRepository;

    public boolean isOneOrDescendant(Position subject, String ascendantCode) {
        if (subject.getCodeName().equals(ascendantCode)) return true;

        List<Position> positions = positionRepository.findAllByInheritedPositionsContainingCodeName(ascendantCode);
        for (Position position : positions) {
            boolean found = isOneOrDescendant(subject, position.getCodeName());
            if (found) return true;
        }

        return false;
    }
}
