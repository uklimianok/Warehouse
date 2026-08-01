package com.warehouse.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.service.PositionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    @Override
    public Position findById(long id) {
        Optional<Position> position = positionRepository.findById(id);
        if (position.isPresent()) return position.get();
        else throw new EntityNotFoundException("Position not found.");
    }

    @Override
    public Position save(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public void delete(long id) {
        boolean positionExists = positionRepository.existsById(id);
        if (positionExists) {
            boolean activeEmployeeExists = employeeRepository.existsByPositionId(id);
            if (activeEmployeeExists) throw new DataIntegrityViolationException("Position is active.");
            else positionRepository.deleteById(id);
        } else throw new EntityNotFoundException("Position not found.");
    }
}
