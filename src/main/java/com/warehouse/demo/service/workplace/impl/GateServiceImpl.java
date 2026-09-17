package com.warehouse.demo.service.workplace.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.workplace.gate.GateRequest;
import com.warehouse.demo.entity.workplace.Gate;
import com.warehouse.demo.mapper.workplace.gate.GateRequestMapper;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.workplace.GateRepository;
import com.warehouse.demo.repository.workplace.TrackRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.workplace.GateService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GateServiceImpl extends AbstractService<Gate, Long> implements GateService {
    private final GateRepository gateRepository;
    private final TrackRepository trackRepository;
    private final OrderRepository orderRepository;

    private final GateRequestMapper gateRequestMapper;

    @Override 
    @Cacheable(value = "gates", key = "#id")
    public Gate read(Long id) {
        return super.read(id);
    }
    
    @Override
    public Gate create(GateRequest gateRequest) {
        if (gateRepository.existsBySymbol(gateRequest.getSymbol()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(new Gate(), gateRequest);
    }

    @Override
    @CacheEvict(value = "gates", key = "#id")
    public Gate update(long id, GateRequest gateRequest) {
        Gate gate = read(id);
        boolean gateChanged = !gate.getSymbol().equals(gateRequest.getSymbol());
        boolean gateExists = gateRepository.existsBySymbol(gateRequest.getSymbol());
        if (gateChanged && gateExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(gate, gateRequest);
    }

    @Override 
    @CacheEvict(value = "gates", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private Gate modifyAndSave(Gate target, GateRequest from) {
        gateRequestMapper.convertFromRequest(from, target);
        return gateRepository.save(target);
    }

    @Override
    protected JpaRepository<Gate, Long> getRepository() {
        return gateRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.GATE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInTrack = trackRepository.existsByGateId(id);
        boolean activeInOrder = orderRepository.existsByGateId(id);
        return activeInTrack || activeInOrder;
    }
}
