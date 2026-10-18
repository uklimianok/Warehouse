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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import local.warehouse.app.configuration.security.UserPrincipal;
import local.warehouse.app.dto.service.status.StatusRequest;
import local.warehouse.app.dto.service.status.StatusResponse;
import local.warehouse.app.entity.service.Status;
import local.warehouse.app.mapper.service.status.StatusResponseMapper;
import local.warehouse.app.service.warehouseService.StatusService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;
    private final StatusResponseMapper statusResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends StatusResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Status> statuses = statusService.readAll();
        List<? extends StatusResponse> statusResponse = statuses
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends StatusResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        Status status = statusService.read(id);
        StatusResponse statusResponse = returnObjectResponse(status, userPrincipal);

        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @GetMapping(params = {"name", "type"})
    public ResponseEntity<? extends StatusResponse> readByNameAndType(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String name, 
        @RequestParam String type
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        Status status = statusService.readByNameAndType(name, type);
        StatusResponse statusResponse = returnObjectResponse(status, userPrincipal);

        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends StatusResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid StatusRequest statusRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Status status = statusService.create(statusRequest);
        StatusResponse statusResponse = returnObjectResponse(status, userPrincipal);

        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends StatusResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid StatusRequest statusRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Status status = statusService.update(id, statusRequest);
        StatusResponse statusResponse = returnObjectResponse(status, userPrincipal);

        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        statusService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private StatusResponse returnObjectResponse(
        Status from, 
        UserPrincipal principal
    ) {
        StatusResponse response = statusResponseMapper.convertToResponse(from);
        return response;
    }
}
