package com.warehouse.demo.service.employee.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.service.AbstractCrudService;
import com.warehouse.demo.service.employee.PositionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl extends AbstractCrudService<Position, Long> implements PositionService {
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    protected JpaRepository<Position, Long> getRepository() {
        return positionRepository;
    }

    @Override
    protected String getEntityName() {
        return "Position";
    }

    @Override
    protected boolean isUsed(Long id) {
        return employeeRepository.existsByPositionId(id);
    }

    @Override
    public Position create(PositionRequest positionRequest) {
        if (positionRepository.existsByName(positionRequest.getName())) 
            throw new DataIntegrityViolationException("Position already exists.");

        Position position = new Position();
        position.setName(positionRequest.getName());
        position.setCodeName(position.getName().replace(' ', '_').toUpperCase());

        return positionRepository.save(position);
    }

    @Override
    public Position update(long id, PositionRequest positionRequest) {
        Position position = read(id);
        position.setName(positionRequest.getName());

        return positionRepository.save(position);
    }
}
