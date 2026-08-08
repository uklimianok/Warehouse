package com.warehouse.demo.service.employee.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.service.employee.PositionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Position> readAll() {
        return positionRepository.findAll();
    }

    @Override
    public Position read(long id) {
        Optional<Position> position = positionRepository.findById(id);
        if (position.isPresent()) return position.get();
        else throw new EntityNotFoundException("Position not found.");
    }

    @Override
    public Position create(PositionRequest positionRequest) {
        boolean positionExists = positionRepository.existsByName(positionRequest.getName());
        if (positionExists) throw new DataIntegrityViolationException("Position already exists.");

        Position position = new Position();
        position.setName(positionRequest.getName());
        position.setCodeName(position.getName().replace(' ', '_').toUpperCase());

        return positionRepository.save(position);
    }

    @Override
    public Position update(long id, PositionRequest positionRequest) {
        Optional<Position> position = positionRepository.findById(id);
        if (position.isPresent()) {
            Position updatedPosition = position.get();
            updatedPosition.setName(positionRequest.getName());

            return positionRepository.save(updatedPosition);
        } else throw new EntityNotFoundException("Position not found.");
    }

    @Override
    public void deleteById(long id) {
        if (positionRepository.existsById(id)) {
            if (employeeRepository.existsByPositionId(id)) 
                throw new DataIntegrityViolationException("Position is active.");
            else positionRepository.deleteById(id);
        } else throw new EntityNotFoundException("Position not found.");
    }
}
