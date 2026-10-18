package local.warehouse.app.service.workplace.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.workplace.workshop.WorkshopRequest;
import local.warehouse.app.entity.workplace.Workshop;
import local.warehouse.app.mapper.workplace.workshop.WorkshopRequestMapper;
import local.warehouse.app.repository.workplace.WorkStationRepository;
import local.warehouse.app.repository.workplace.WorkshopRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.workplace.WorkshopService;
import local.warehouse.app.util.info.Entity;
import lombok.RequiredArgsConstructor;

@Service
@CacheConfig(cacheNames = "workshops")
@RequiredArgsConstructor
public class WorkshopServiceImpl extends AbstractService<Workshop, Long> implements WorkshopService {
    private final WorkshopRepository workshopRepository;
    private final WorkStationRepository workStationRepository;

    private final WorkshopRequestMapper workshopRequestMapper;
    
    @Override 
    @Cacheable(key = "#id")
    public Workshop read(Long id) {
        return super.read(id);
    }

    @Override
    public Workshop create(WorkshopRequest workshopRequest) {
        return modifyAndSave(new Workshop(), workshopRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public Workshop update(long id, WorkshopRequest workshopRequest) {
        return modifyAndSave(read(id), workshopRequest);
    }

    @Override 
    @CacheEvict(key = "#id") 
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
    protected Entity getEntityName() {
        return Entity.WORKSHOP;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInWorkStation = workStationRepository.existsByWorkshopId(id);
        return activeInWorkStation;
    }
}
