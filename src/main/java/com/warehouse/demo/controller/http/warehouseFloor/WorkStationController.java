package com.warehouse.demo.controller.http.warehouseFloor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.workplace.workStation.WorkStationRequest;
import com.warehouse.demo.dto.workplace.workStation.WorkStationResponse;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.mapper.workplace.workStation.WorkStationResponseMapper;
import com.warehouse.demo.service.workplace.WorkStationService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/work-stations")
@RequiredArgsConstructor
public class WorkStationController {
    private final WorkStationService workStationService;
    private final WorkStationResponseMapper workStationResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends WorkStationResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());
        
        List<WorkStation> workStations = workStationService.readAll();
        List<? extends WorkStationResponse> workStationsResponse = workStations
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        return new ResponseEntity<>(workStationsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends WorkStationResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        WorkStation workStation = workStationService.read(id);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        return new ResponseEntity<>(workStationResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends WorkStationResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody WorkStationRequest workStationRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());
        
        WorkStation workStation = workStationService.create(workStationRequest);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        return new ResponseEntity<>(workStationResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends WorkStationResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody WorkStationRequest workStationRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());
        
        WorkStation workStation = workStationService.update(id, workStationRequest);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        return new ResponseEntity<>(workStationResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        workStationService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private WorkStationResponse returnObjectResponse(WorkStation from, UserPrincipal principal) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> workStationResponseMapper.convertToFullResponse(from);
            default -> workStationResponseMapper.convertToResponse(from);
        };
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
