package local.warehouse.app.mapper.employee.shift;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.employee.Shift;
import local.warehouse.app.repository.employee.ShiftRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShiftResolver {
    private final ShiftRepository shiftRepository;

    public Shift mapShift(long shiftId) {
        return shiftRepository.findById(shiftId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.SHIFT, OutputMessage.NOT_FOUND)));
    }
}
