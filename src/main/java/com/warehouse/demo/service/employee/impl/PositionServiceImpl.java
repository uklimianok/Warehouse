package com.warehouse.demo.service.employee.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.PositionService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl extends AbstractService<Position, Long> implements PositionService {
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    protected JpaRepository<Position, Long> getRepository() {
        return positionRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.POSITION;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInEmployee = employeeRepository.existsByPositionId(id);
        return activeInEmployee;
    }

    @Override
    public Position create(PositionRequest positionRequest) {
        if (positionRepository.existsByName(positionRequest.getName())) 
            throw new DataIntegrityViolationException(Utility.getOutputMessage(EntityName.POSITION, OutputMessage.EXISTS));

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
