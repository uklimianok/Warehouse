package com.warehouse.demo.service.workplace.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.workplace.workshop.WorkshopRequest;
import com.warehouse.demo.entity.workplace.Workshop;
import com.warehouse.demo.mapper.workplace.workshop.WorkshopRequestMapper;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.repository.workplace.WorkshopRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.workplace.WorkshopService;
import com.warehouse.demo.util.EntityName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkshopServiceImpl extends AbstractService<Workshop, Long> implements WorkshopService {
    private final WorkshopRepository workshopRepository;
    private final WorkStationRepository workStationRepository;

    private final WorkshopRequestMapper workshopRequestMapper;
    
    @Override 
    @Cacheable(value = "workshops", key = "#id")
    public Workshop read(Long id) {
        return super.read(id);
    }

    @Override
    public Workshop create(WorkshopRequest workshopRequest) {
        return modifyAndSave(new Workshop(), workshopRequest);
    }

    @Override
    @CacheEvict(value = "workshops", key = "#id")
    public Workshop update(long id, WorkshopRequest workshopRequest) {
        return modifyAndSave(read(id), workshopRequest);
    }

    @Override 
    @CacheEvict(value = "workshops", key = "#id") 
    public void delete(Long id) {
        super.delete(id);
    }

    private Workshop modifyAndSave(Workshop target, WorkshopRequest from) {
        workshopRequestMapper.convertFromRequest(from, target);
        return workshopRepository.save(target);
    }

    @Override
    protected JpaRepository<Workshop, Long> getRepository() {
        return workshopRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.WORKSHOP;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInWorkStation = workStationRepository.existsByWorkshopId(id);
        return activeInWorkStation;
    }
}
