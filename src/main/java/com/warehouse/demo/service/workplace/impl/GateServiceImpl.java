package com.warehouse.demo.service.workplace.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.workplace.gate.GateRequest;
import com.warehouse.demo.entity.workplace.Gate;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.workplace.GateRepository;
import com.warehouse.demo.repository.workplace.TrackRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.workplace.GateService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GateServiceImpl extends AbstractService<Gate, Long> implements GateService {
    private final GateRepository gateRepository;
    private final TrackRepository trackRepository;
    private final OrderRepository orderRepository;
    
    @Override
    public Gate create(GateRequest gateRequest) {
        if (gateRepository.existsBySymbol(gateRequest.getSymbol()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        Gate gate = new Gate();
        return modifyAndSave(gate, gateRequest);
    }

    @Override
    public Gate update(long id, GateRequest gateRequest) {
        Gate gate = read(id);
        boolean gateChanged = !gate.getSymbol().equals(gateRequest.getSymbol());
        boolean gateExists = gateRepository.existsBySymbol(gateRequest.getSymbol());
        if (gateChanged && gateExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(gate, gateRequest);
    }

    @Override
    protected JpaRepository<Gate, Long> getRepository() {
        return gateRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.GATE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInTrack = trackRepository.existsByGateId(id);
        boolean activeInOrder = orderRepository.existsByGateId(id);
        return activeInTrack || activeInOrder;
    }

    private Gate modifyAndSave(Gate target, GateRequest from) {
        target.setSymbol(from.getSymbol());

        return gateRepository.save(target);
    }
}
