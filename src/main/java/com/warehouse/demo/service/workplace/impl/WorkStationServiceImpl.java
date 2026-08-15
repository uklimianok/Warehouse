package com.warehouse.demo.service.workplace.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.workplace.workStation.WorkStationRequest;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.repository.workplace.WorkshopRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.workplace.WorkStationService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkStationServiceImpl extends AbstractService<WorkStation, Long> implements WorkStationService {
    private final WorkStationRepository workStationRepository;
    private final WorkshopRepository workshopRepository;
    private final ProductPalletRepository productPalletRepository;
    
    @Override
    public WorkStation create(WorkStationRequest workStationRequest) {
        boolean stationNameExists = workStationRepository.existsByStationNumber(workStationRequest.getStationNumber());
        if (stationNameExists) 
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));
        
        WorkStation workStation = new WorkStation();
        return modifyAndSave(workStation, workStationRequest);
    }

    @Override
    public WorkStation update(long id, WorkStationRequest workStationRequest) {
        WorkStation workStation = read(id);
        boolean stationNumberChanged = !workStation.getStationNumber().equals(workStationRequest.getStationNumber());
        boolean stationNumberExists = workStationRepository.existsByStationNumber(workStationRequest.getStationNumber());
        if (stationNumberChanged && stationNumberExists) 
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(workStation, workStationRequest);
    }

    @Override
    protected JpaRepository<WorkStation, Long> getRepository() {
        return workStationRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.WORK_STATION;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInProductPallet = productPalletRepository.existsByWorkStationId(id);
        return activeInProductPallet;
    }

    private WorkStation modifyAndSave(WorkStation target, WorkStationRequest from) {
        target.setStationNumber(from.getStationNumber());
        target.setControlNumber(from.getControlNumber());
        target.setType(from.getType());
        target.setWorkshop(
            workshopRepository
                .findById(from.getWorkshopId())
                .orElseThrow(() -> 
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.WORKSHOP, OutputMessage.NOT_FOUND))
                )

        );

        return workStationRepository.save(target);
    }
}
