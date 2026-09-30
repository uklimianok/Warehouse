package com.warehouse.demo.service.employee.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.mapper.employee.position.PositionRequestMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.PositionService;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl extends AbstractService<Position, Long> implements PositionService {
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;

    private final PositionRequestMapper positionRequestMapper;

    private final String LOOP_INHERITANCE_MESSAGE = "This position is already in the chain of position inheritance.";

    @Override
    @Cacheable(value = "positions", key = "#id")
    public Position read(Long id) {
        return super.read(id);
    }

    @Override
    public Position create(PositionRequest positionRequest) {
        if (positionRepository.existsByName(positionRequest.getName())) 
            throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        Position position = new Position();
        position.setCodeName(position.getName().replace(' ', '_').toUpperCase());
        position.setRemovable(true);

        return modifyAndSave(position, positionRequest);
    }

    @Override
    @CacheEvict(value = "positions", key = "#id")
    public Position update(long id, PositionRequest positionRequest) {
        Position position = read(id);
        boolean nameChanged = !position.getName().equals(positionRequest.getName());
        boolean nameExists = positionRepository.existsByName(positionRequest.getName());
        if (nameChanged && nameExists)
            throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        throwIfInheritanceLooped(position.getId(), positionRequest.getInheritedPositionsId());

        return modifyAndSave(position, positionRequest);
    }

    @Override 
    @CacheEvict(value = "positions", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private Position modifyAndSave(Position target, PositionRequest from) {
        positionRequestMapper.convertFromRequest(from, target);
        return positionRepository.save(target);
    }

    @Override
    protected JpaRepository<Position, Long> getRepository() {
        return positionRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.POSITION;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInEmployee = employeeRepository.existsByPositionId(id);
        return activeInEmployee;
    }

    private void throwIfInheritanceLooped(long targetId, List<Long> parents) {
        if (parents == null) return;

        for (int i = 0; i < parents.size(); i++) {
            if (parents.get(i) == targetId)
                throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(LOOP_INHERITANCE_MESSAGE));
        
            List<Long> grandParents = positionRepository.findInheritedPositionsById(parents.get(i));
            throwIfInheritanceLooped(targetId, grandParents);
        }
    }
}
