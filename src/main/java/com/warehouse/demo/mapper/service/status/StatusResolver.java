package com.warehouse.demo.mapper.service.status;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class StatusResolver {
    private final StatusRepository statusRepository;

    public Status mapStatus(long statusId) {
        return statusRepository.findById(statusId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.STATUS, OutputMessage.NOT_FOUND)));
    }
}
