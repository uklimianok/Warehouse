package com.warehouse.demo.controller.http.officeManagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.warehouse.demo.dto.employee.shift.ShiftRequest;
import com.warehouse.demo.dto.employee.shift.ShiftResponse;
import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.mapper.employee.shift.ShiftResponseMapper;
import com.warehouse.demo.service.employee.ShiftService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;
    private final ShiftResponseMapper shiftResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends ShiftResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Shift> shifts = shiftService.readAll();
        List<ShiftResponse> shiftResponses = shifts
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        return new ResponseEntity<>(shiftResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ShiftResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        Shift shift = shiftService.read(id);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        return new ResponseEntity<>(shiftResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends ShiftResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid ShiftRequest shiftRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Shift shift = shiftService.create(shiftRequest);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        return new ResponseEntity<>(shiftResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends ShiftResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid ShiftRequest shiftRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Shift shift = shiftService.update(id, shiftRequest);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        return new ResponseEntity<>(shiftResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        shiftService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.SHIFT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private ShiftResponse returnObjectResponse(
        Shift from, 
        UserPrincipal principal
    ) {
        ShiftResponse response = shiftResponseMapper.convertToResponse(from);
        return response;
    }
}
