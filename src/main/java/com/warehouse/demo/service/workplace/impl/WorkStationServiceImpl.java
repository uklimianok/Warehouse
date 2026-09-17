package com.warehouse.demo.service.workplace.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.workplace.workStation.WorkStationRequest;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.mapper.workplace.workStation.WorkStationRequestMapper;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.workplace.WorkStationService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkStationServiceImpl extends AbstractService<WorkStation, Long> implements WorkStationService {
    private final WorkStationRepository workStationRepository;
    private final ProductPalletRepository productPalletRepository;

    private final WorkStationRequestMapper workStationRequestMapper;
    
    @Override
    @Cacheable(value = "workStations", key = "#id")
    public WorkStation read(Long id) {
        return super.read(id);
    }

    @Override
    public WorkStation create(WorkStationRequest workStationRequest) {
        boolean stationNameExists = workStationRepository.existsByStationNumber(workStationRequest.getStationNumber());
        if (stationNameExists) 
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));
        
        return modifyAndSave(new WorkStation(), workStationRequest);
    }

    @Override
    @CacheEvict(value = "workStations", key = "#id")
    public WorkStation update(long id, WorkStationRequest workStationRequest) {
        WorkStation workStation = read(id);
        boolean stationNumberChanged = !workStation.getStationNumber().equals(workStationRequest.getStationNumber());
        boolean stationNumberExists = workStationRepository.existsByStationNumber(workStationRequest.getStationNumber());
        if (stationNumberChanged && stationNumberExists) 
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(workStation, workStationRequest);
    }

    @Override 
    @CacheEvict(value = "workStations", key = "#id")
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
