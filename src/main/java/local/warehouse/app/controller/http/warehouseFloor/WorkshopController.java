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
import local.warehouse.app.dto.workplace.workshop.WorkshopRequest;
import local.warehouse.app.dto.workplace.workshop.WorkshopResponse;
import local.warehouse.app.entity.workplace.Workshop;
import local.warehouse.app.mapper.workplace.workshop.WorkshopResponseMapper;
import local.warehouse.app.service.workplace.WorkshopService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class WorkshopController {
    private final WorkshopService workshopService;
    private final WorkshopResponseMapper workshopResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends WorkshopResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Workshop> workshops = workshopService.readAll();
        List<? extends WorkshopResponse> workshopResponse = workshops
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        return new ResponseEntity<>(workshopResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends WorkshopResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');
        
        Workshop workshop = workshopService.read(id);
        WorkshopResponse workshopResponse = returnObjectResponse(workshop, userPrincipal);

        return new ResponseEntity<>(workshopResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends WorkshopResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid WorkshopRequest workshopRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');
        
        Workshop workshop = workshopService.create(workshopRequest);
        WorkshopResponse workshopResponse = returnObjectResponse(workshop, userPrincipal);

        return new ResponseEntity<>(workshopResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends WorkshopResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid WorkshopRequest workshopRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');
        
        Workshop workshop = workshopService.update(id, workshopRequest);
        WorkshopResponse workshopResponse = returnObjectResponse(workshop, userPrincipal);

        return new ResponseEntity<>(workshopResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        workshopService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.WORKSHOP, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private WorkshopResponse returnObjectResponse(
        Workshop from, 
        UserPrincipal principal
    ) {
        WorkshopResponse response = workshopResponseMapper.convertToResponse(from);
        return response;
    }
}
