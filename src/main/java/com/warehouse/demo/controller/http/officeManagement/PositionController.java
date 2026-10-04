package com.warehouse.demo.controller.http.officeManagement;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.dto.employee.position.PositionResponse;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.mapper.employee.position.PositionResponseMapper;
import com.warehouse.demo.service.employee.PositionService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;
    private final PositionResponseMapper positionResponseMapper;
    
    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends PositionResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Position> positions = positionService.readAll();
        List<? extends PositionResponse> positionsResponse = positions
            .stream()
            .map(p -> returnPositionResponse(p, userPrincipal))
            .toList();

        return new ResponseEntity<>(positionsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends PositionResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');
        
        Position position = positionService.read(id);
        PositionResponse positionResponse = returnPositionResponse(position, userPrincipal);

        return new ResponseEntity<>(positionResponse, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<? extends PositionResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid PositionRequest positionRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Position position = positionService.create(positionRequest);
        PositionResponse positionResponse = returnPositionResponse(position, userPrincipal);
        
        return new ResponseEntity<>(positionResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends PositionResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid PositionRequest positionRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Position position = positionService.update(id, positionRequest);
        PositionResponse positionResponse = returnPositionResponse(position, userPrincipal);
        
        return new ResponseEntity<>(positionResponse, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        positionService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.POSITION, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private PositionResponse returnPositionResponse(
        Position from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> positionResponseMapper.convertToFullResponse(from);
            default -> positionResponseMapper.convertToResponse(from);
        };
    }
}
