package local.warehouse.app.mapper.workplace.gate;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.workplace.Gate;
import local.warehouse.app.repository.workplace.GateRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class GateResolver {
    private final GateRepository gateRepository;

    public Gate mapGate(long gateId) {
        return gateRepository.findById(gateId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.GATE, OutputMessage.NOT_FOUND)));    
    }
}
