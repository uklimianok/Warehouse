package com.warehouse.demo.mapper.workplace.workStation;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class WorkStationResolver {
    private final WorkStationRepository workStationRepository;

    public WorkStation mapWorkStation(long workStationId) {
        return workStationRepository.findById(workStationId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(Entity.WORK_STATION, OutputMessage.NOT_FOUND)));
    }
}
