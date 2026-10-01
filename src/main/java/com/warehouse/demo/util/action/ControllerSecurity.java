package com.warehouse.demo.util.action;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class ControllerSecurity {
    private final PositionRepository positionRepository;

    public List<String> getAccessRoles(Class<?> controller, char mode) {
        return positionRepository.findCodeNameByControllerFlagsContaining(controller.getSimpleName(), String.valueOf(mode));
    }

    public String getResponseObjectType(Class<?> controller, String employeeNumber) {
        return positionRepository.findControllerFlags_ResponseObjectTypeByEmployee_EmployeeNumberAndControllerFlagsEquals(controller.getSimpleName(), employeeNumber)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.RESPONSE_OBJECT, OutputMessage.NOT_FOUND)));
    }
}
