package com.warehouse.demo.controller.warehouseFloor;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.dto.workplace.workStation.FullWorkStationResponse;
import com.warehouse.demo.dto.workplace.workStation.OperatorWorkStationResponse;
import com.warehouse.demo.dto.workplace.workStation.WorkStationRequest;
import com.warehouse.demo.dto.workplace.workStation.WorkStationResponse;
import com.warehouse.demo.dto.workplace.workshop.WorkshopResponse;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.workplace.WorkStationService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/work_stations")
@RequiredArgsConstructor
public class WorkStationController {
    private final WorkStationService workStationService;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('GOODS_UNLOADER', 'GOODS_PICKER', 'OPERATOR', " + 
        "'COORDINATOR', 'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', " +
        "'DIRECTOR', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";

    private static final String[] OPERATOR_RESPONSE_ROLES_ARR = 
        {
            "OPERATOR"
        };
    private static final String[] FULL_RESPONSE_ROLES_ARR =
        {
            "COORDINATOR", "DATA_CONTROLLER", "SHIFT_SUPERVISOR", 
            "DIRECTOR", "DEVELOPER", "SYSTEM_ADMINISTRATOR"
        };

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends WorkStationResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<WorkStation> workStations = workStationService.readAll();
        List<? extends WorkStationResponse> workStationsResponse = workStations
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        ResponseEntity<List<? extends WorkStationResponse>> response = new ResponseEntity<>(workStationsResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<WorkStationResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        WorkStation workStation = workStationService.read(id);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        ResponseEntity<WorkStationResponse> response = new ResponseEntity<>(workStationResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<WorkStationResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody WorkStationRequest workStationRequest) {
        WorkStation workStation = workStationService.create(workStationRequest);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        ResponseEntity<WorkStationResponse> response = new ResponseEntity<>(workStationResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<WorkStationResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody WorkStationRequest workStationRequest) {
        WorkStation workStation = workStationService.update(id, workStationRequest);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        ResponseEntity<WorkStationResponse> response = new ResponseEntity<>(workStationResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        workStationService.delete(id);
        String message = Utility.getOutputMessage(EntityName.WORK_STATION, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private WorkStationResponse returnObjectResponse(WorkStation from, UserPrincipal userPrincipal) {
        WorkStationResponse workStationResponse = null;
        if (userPrincipal.hasAnyRole(FULL_RESPONSE_ROLES_ARR))
            workStationResponse = new FullWorkStationResponse(
                from.getId(),
                from.getStationNumber(),
                from.getControlNumber(),
                from.getType(),
                new WorkshopResponse(
                    from.getWorkshop().getId(),
                    from.getWorkshop().getName(),
                    from.getWorkshop().getStandard()
                )
            );
        else if (userPrincipal.hasAnyRole(OPERATOR_RESPONSE_ROLES_ARR))
            workStationResponse = new OperatorWorkStationResponse(
                from.getId(),
                from.getStationNumber(),
                from.getControlNumber(),
                from.getType()
            );
        else
            workStationResponse = new WorkStationResponse(
                from.getId(),
                from.getStationNumber(),
                from.getControlNumber()
            );


        return workStationResponse;
    }
}
