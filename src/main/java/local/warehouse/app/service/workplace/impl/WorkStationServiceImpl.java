package local.warehouse.app.service.workplace.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.workplace.workStation.WorkStationRequest;
import local.warehouse.app.entity.workplace.WorkStation;
import local.warehouse.app.mapper.workplace.workStation.WorkStationRequestMapper;
import local.warehouse.app.repository.product.ProductPalletRepository;
import local.warehouse.app.repository.workplace.WorkStationRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.workplace.WorkStationService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkStationServiceImpl extends AbstractService<WorkStation, Long> implements WorkStationService {
    private final WorkStationRepository workStationRepository;
    private final ProductPalletRepository productPalletRepository;

    private final WorkStationRequestMapper workStationRequestMapper;
    
    @Override
    public WorkStation read(Long id) {
        return super.read(id);
    }

    @Override
    public WorkStation create(WorkStationRequest workStationRequest) {
        boolean stationNameExists = workStationRepository.existsByStationNumber(workStationRequest.getStationNumber());
        if (stationNameExists) 
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));
        
        return modifyAndSave(new WorkStation(), workStationRequest);
    }

    @Override
    public WorkStation update(long id, WorkStationRequest workStationRequest) {
        WorkStation workStation = read(id);
        boolean stationNumberChanged = !workStation.getStationNumber().equals(workStationRequest.getStationNumber());
        boolean stationNumberExists = workStationRepository.existsByStationNumber(workStationRequest.getStationNumber());
        if (stationNumberChanged && stationNumberExists) 
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(workStation, workStationRequest);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    private WorkStation modifyAndSave(WorkStation target, WorkStationRequest from) {
        workStationRequestMapper.convertFromRequest(from, target);
        return workStationRepository.save(target);
    }

    @Override
    protected JpaRepository<WorkStation, Long> getRepository() {
        return workStationRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.WORK_STATION;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInProductPalletWorkStationId = productPalletRepository.existsByWorkStationId(id);
        boolean activeInProductPalletNextWorkStationId = productPalletRepository.existsByNextWorkStationId(id);
        return activeInProductPalletWorkStationId || activeInProductPalletNextWorkStationId;
    }
}
