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
import com.warehouse.demo.dto.workplace.gate.GateRequest;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.workplace.Gate;
import com.warehouse.demo.mapper.workplace.gate.GateResponseMapper;
import com.warehouse.demo.service.workplace.GateService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gates")
@RequiredArgsConstructor
public class GateController {
    private final GateService gateService;
    private final GateResponseMapper gateResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends GateResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<Gate> gates = gateService.readAll();
        List<GateResponse> gatesResponse = gates
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        return new ResponseEntity<>(gatesResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends GateResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        Gate gate = gateService.read(id);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        return new ResponseEntity<>(gateResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends GateResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody GateRequest gateRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());

        Gate gate = gateService.create(gateRequest);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        return new ResponseEntity<>(gateResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends GateResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody GateRequest gateRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());

        Gate gate = gateService.update(id, gateRequest);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        return new ResponseEntity<>(gateResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        gateService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.GATE, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private GateResponse returnObjectResponse(Gate from, UserPrincipal principal) {
        GateResponse response = gateResponseMapper.convertToResponse(from);
        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}