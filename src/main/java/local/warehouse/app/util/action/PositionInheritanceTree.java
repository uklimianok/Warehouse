package local.warehouse.app.util.action;

import org.springframework.stereotype.Component;

import local.warehouse.app.entity.employee.Position;
import local.warehouse.app.repository.employee.PositionRepository;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class PositionInheritanceTree {
    private final PositionRepository positionRepository;

    public boolean isOneOrDescendant(Position subject, String ascendantCode) {
        return positionRepository.codeNameIsTargetOrDescendant(ascendantCode, subject.getId());
    }
}
