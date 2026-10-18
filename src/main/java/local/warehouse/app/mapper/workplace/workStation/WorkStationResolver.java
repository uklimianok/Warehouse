package local.warehouse.app.mapper.workplace.workStation;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.workplace.WorkStation;
import local.warehouse.app.repository.workplace.WorkStationRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class WorkStationResolver {
    private final WorkStationRepository workStationRepository;

    public WorkStation mapWorkStation(long workStationId) {
        return workStationRepository.findById(workStationId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)));
    }
}
