package com.warehouse.demo.util.action;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.demo.repository.employee.PositionRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class ControllerSecurity {
    private final PositionRepository positionRepository;

    public List<String> getAccessRoles(Class<?> controller, char mode) {
        return positionRepository.findCodeNameByControllerAccessFlagsContaining(controller.getSimpleName(), String.valueOf(mode));
    }
}
