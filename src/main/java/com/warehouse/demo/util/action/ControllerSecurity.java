package com.warehouse.demo.util.action;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class ControllerSecurity {
    private final PositionRepository positionRepository;

    public void throwIfUnauthorized(Class<?> controller, String employeeNumber, char mode) {
        boolean hasAccess = positionRepository.modeIsContainedInControllerFlagsByEmployeeNumber(controller.getSimpleName(), employeeNumber, String.valueOf(mode));
        if (!hasAccess)
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }

    public String getResponseObjectType(Class<?> controller, String employeeNumber) {
        return positionRepository.findControllerFlags_ResponseObjectTypeByEmployee_EmployeeNumberAndControllerFlagsEquals(controller.getSimpleName(), employeeNumber)
            .orElse("");
    }
}
