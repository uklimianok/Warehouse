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
import local.warehouse.app.dto.workplace.gate.GateRequest;
import local.warehouse.app.dto.workplace.gate.GateResponse;
import local.warehouse.app.entity.workplace.Gate;
import local.warehouse.app.mapper.workplace.gate.GateResponseMapper;
import local.warehouse.app.service.workplace.GateService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gates")
@RequiredArgsConstructor
public class GateController {
    private final GateService gateService;
    private final GateResponseMapper gateResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends GateResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Gate> gates = gateService.readAll();
        List<GateResponse> gatesResponse = gates
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        return new ResponseEntity<>(gatesResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends GateResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        Gate gate = gateService.read(id);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        return new ResponseEntity<>(gateResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends GateResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid GateRequest gateRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Gate gate = gateService.create(gateRequest);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        return new ResponseEntity<>(gateResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends GateResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid GateRequest gateRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Gate gate = gateService.update(id, gateRequest);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        return new ResponseEntity<>(gateResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        gateService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.GATE, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private GateResponse returnObjectResponse(
        Gate from, 
        UserPrincipal principal
    ) {
        GateResponse response = gateResponseMapper.convertToResponse(from);
        return response;
    }
}