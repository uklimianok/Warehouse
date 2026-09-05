package com.warehouse.demo.service.workplace.impl;

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
    public Workshop create(WorkshopRequest workshopRequest) {
        Workshop workshop = new Workshop();
        return modifyAndSave(workshop, workshopRequest);
    }

    @Override
    public Workshop update(long id, WorkshopRequest workshopRequest) {
        Workshop workshop = read(id);
        return modifyAndSave(workshop, workshopRequest);
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

    private Workshop modifyAndSave(Workshop target, WorkshopRequest from) {
        workshopRequestMapper.convertFromRequest(from, target);
        return workshopRepository.save(target);
    }
}
