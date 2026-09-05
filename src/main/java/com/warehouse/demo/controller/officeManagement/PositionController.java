package com.warehouse.demo.controller.officeManagement;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.dto.employee.position.PositionRequest;
import com.warehouse.demo.dto.employee.position.PositionResponse;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.mapper.employee.position.PositionResponseMapper;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.employee.PositionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    
    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('GOODS_UNLOADER', 'GOODS_PICKER', 'SET_GOODS_EXPORTER', 'SET_GOODS_LOADER', " +
        "'OPERATOR', 'RETURN_GOODS_CONTROLLER', 'COORDINATOR', 'DATA_CONTROLLER', " +
        "'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', 'DIRECTOR', 'MAJOR_HR', " +
        "'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES =
        "hasAnyRole('MAJOR_HR', 'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('MAJOR_HR', 'SYSTEM_ADMINISTRATOR')";

    private static final String[] FULL_ACCESS_ROLES_ARR = {"MAJOR_HR", "SYSTEM_ADMINISTRATOR"};

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends PositionResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Position> positions = positionService.readAll();
        List<? extends PositionResponse> positionsResponse = positions
            .stream()
            .map(p -> returnPositionResponse(p, userPrincipal))
            .toList();

        ResponseEntity<List<? extends PositionResponse>> response = new ResponseEntity<>(positionsResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends PositionResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Position position = positionService.read(id);
        PositionResponse positionResponse = returnPositionResponse(position, userPrincipal);

        ResponseEntity<PositionResponse> response = new ResponseEntity<>(positionResponse, HttpStatus.OK);
        return response;
    }
    
    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends PositionResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PositionRequest positionRequest) {
        Position position = positionService.create(positionRequest);
        PositionResponse positionResponse = returnPositionResponse(position, userPrincipal);
        
        ResponseEntity<PositionResponse> response = new ResponseEntity<>(positionResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends PositionResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody PositionRequest positionRequest) {
        Position position = positionService.update(id, positionRequest);
        PositionResponse positionResponse = returnPositionResponse(position, userPrincipal);
        
        ResponseEntity<PositionResponse> response = new ResponseEntity<>(positionResponse, HttpStatus.OK);
        return response;
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        positionService.delete(id);

        ResponseEntity<String> response = new ResponseEntity<>("Position is deleted.", HttpStatus.OK);
        return response;
    }

    private PositionResponse returnPositionResponse(Position from, UserPrincipal principal) {
        PositionResponse response = null;
        if (principal.hasAnyRole(FULL_ACCESS_ROLES_ARR))
            response = positionResponseMapper.convertToFullResponse(from);
        else
            response = positionResponseMapper.convertToResponse(from);

        return response;
    }
}
