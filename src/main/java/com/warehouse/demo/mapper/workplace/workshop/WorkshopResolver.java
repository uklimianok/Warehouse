package com.warehouse.demo.mapper.workplace.workshop;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.workplace.Workshop;
import com.warehouse.demo.repository.workplace.WorkshopRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class WorkshopResolver {
    private final WorkshopRepository workshopRepository;

    public Workshop mapWorkshop(long workshopId) {
        return workshopRepository.findById(workshopId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.WORKSHOP, OutputMessage.NOT_FOUND)));
    }
}
