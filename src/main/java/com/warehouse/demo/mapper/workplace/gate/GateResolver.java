package com.warehouse.demo.mapper.workplace.gate;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.workplace.Gate;
import com.warehouse.demo.repository.workplace.GateRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class GateResolver {
    private final GateRepository gateRepository;

    public Gate mapGate(long gateId) {
        return gateRepository.findById(gateId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.GATE, OutputMessage.NOT_FOUND)));    
    }
}
