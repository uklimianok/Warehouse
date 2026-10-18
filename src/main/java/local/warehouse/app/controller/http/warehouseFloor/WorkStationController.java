package local.warehouse.app.controller.http.warehouseFloor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import local.warehouse.app.configuration.security.UserPrincipal;
import local.warehouse.app.dto.workplace.workStation.WorkStationRequest;
import local.warehouse.app.dto.workplace.workStation.WorkStationResponse;
import local.warehouse.app.entity.workplace.WorkStation;
import local.warehouse.app.mapper.workplace.workStation.WorkStationResponseMapper;
import local.warehouse.app.service.workplace.WorkStationService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/work-stations")
@RequiredArgsConstructor
public class WorkStationController {
    private final WorkStationService workStationService;
    private final WorkStationResponseMapper workStationResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends WorkStationResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');
        
        List<WorkStation> workStations = workStationService.readAll();
        List<? extends WorkStationResponse> workStationsResponse = workStations
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        return new ResponseEntity<>(workStationsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends WorkStationResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        WorkStation workStation = workStationService.read(id);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        return new ResponseEntity<>(workStationResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends WorkStationResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid WorkStationRequest workStationRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');
        
        WorkStation workStation = workStationService.create(workStationRequest);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        return new ResponseEntity<>(workStationResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends WorkStationResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid WorkStationRequest workStationRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');
        
        WorkStation workStation = workStationService.update(id, workStationRequest);
        WorkStationResponse workStationResponse = returnObjectResponse(workStation, userPrincipal);

        return new ResponseEntity<>(workStationResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        workStationService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.WORK_STATION, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private WorkStationResponse returnObjectResponse(
        WorkStation from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> workStationResponseMapper.convertToFullResponse(from);
            default -> workStationResponseMapper.convertToResponse(from);
        };
    }
}
