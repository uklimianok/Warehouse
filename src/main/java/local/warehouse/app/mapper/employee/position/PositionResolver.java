package local.warehouse.app.mapper.employee.position;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.employee.Position;
import local.warehouse.app.repository.employee.PositionRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionResolver {
    private final PositionRepository positionRepository;

    public Position mapPosition(long positionId) {
        return positionRepository.findById(positionId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.POSITION, OutputMessage.NOT_FOUND)));
    }
}
