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

import com.warehouse.demo.dto.workplace.gate.GateRequest;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.workplace.Gate;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.workplace.GateService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gates")
@RequiredArgsConstructor
public class GateController {
    private final GateService gateService;

    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'SET_GOODS_EXPORTER', 'SET_GOODS_LOADER', " +
        "'COORDINATOR', 'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', " +
        "'DIRECTOR', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends GateResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Gate> gates = gateService.readAll();
        List<GateResponse> gatesResponse = gates
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        ResponseEntity<List<? extends GateResponse>> response = new ResponseEntity<>(gatesResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends GateResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Gate gate = gateService.read(id);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        ResponseEntity<GateResponse> response = new ResponseEntity<>(gateResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends GateResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody GateRequest gateRequest) {
        Gate gate = gateService.create(gateRequest);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        ResponseEntity<GateResponse> response = new ResponseEntity<>(gateResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends GateResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody GateRequest gateRequest) {
        Gate gate = gateService.update(id, gateRequest);
        GateResponse gateResponse = returnObjectResponse(gate, userPrincipal);

        ResponseEntity<GateResponse> response = new ResponseEntity<>(gateResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        gateService.delete(id);
        String message = Utility.getOutputMessage(EntityName.GATE, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private GateResponse returnObjectResponse(Gate from, UserPrincipal userPrincipal) {
        GateResponse palletResponse = new GateResponse(
            from.getId(),
            from.getSymbol()
        );

        return palletResponse;
    }
}