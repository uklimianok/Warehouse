package local.warehouse.app.mapper.service.status;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.service.Status;
import local.warehouse.app.repository.service.StatusRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class StatusResolver {
    private final StatusRepository statusRepository;

    public Status mapStatus(long statusId) {
        return statusRepository.findById(statusId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));
    }
}
