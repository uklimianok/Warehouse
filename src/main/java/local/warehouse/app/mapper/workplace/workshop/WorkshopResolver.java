package local.warehouse.app.mapper.workplace.workshop;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.workplace.Workshop;
import local.warehouse.app.repository.workplace.WorkshopRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class WorkshopResolver {
    private final WorkshopRepository workshopRepository;

    public Workshop mapWorkshop(long workshopId) {
        return workshopRepository.findById(workshopId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.WORKSHOP, OutputMessage.NOT_FOUND)));
    }
}
