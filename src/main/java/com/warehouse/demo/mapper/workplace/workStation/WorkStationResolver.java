package com.warehouse.demo.mapper.workplace.workStation;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.repository.workplace.WorkStationRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class WorkStationResolver {
    private final WorkStationRepository workStationRepository;

    public WorkStation mapWorkStation(long workStationId) {
        return workStationRepository.findById(workStationId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.WORK_STATION, OutputMessage.NOT_FOUND)));
    }
}
