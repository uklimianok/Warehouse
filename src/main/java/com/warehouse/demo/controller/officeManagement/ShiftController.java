package com.warehouse.demo.controller.officeManagement;

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

import com.warehouse.demo.dto.employee.shift.ShiftRequest;
import com.warehouse.demo.dto.employee.shift.ShiftResponse;
import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.employee.ShiftService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('SHIFT_SUPERVISOR', 'DIRECTOR', 'MAJOR_HR', " +
        "'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', 'STATISTICS_PROCEEDER', " +
        "'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('MAJOR_HR', 'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', " +
        "'SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends ShiftResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Shift> shifts = shiftService.readAll();
        List<ShiftResponse> shiftResponses = shifts
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        ResponseEntity<List<? extends ShiftResponse>> response = new ResponseEntity<>(shiftResponses, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends ShiftResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Shift shift = shiftService.read(id);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        ResponseEntity<ShiftResponse> response = new ResponseEntity<>(shiftResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends ShiftResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ShiftRequest shiftRequest) {
        Shift shift = shiftService.create(shiftRequest);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        ResponseEntity<ShiftResponse> response = new ResponseEntity<>(shiftResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends ShiftResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ShiftRequest shiftRequest) {
        Shift shift = shiftService.update(id, shiftRequest);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        ResponseEntity<ShiftResponse> response = new ResponseEntity<>(shiftResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        shiftService.delete(id);

        ResponseEntity<String> response = new ResponseEntity<>("Shift deleted.", HttpStatus.OK);
        return response;
    }

    private ShiftResponse returnObjectResponse(Shift from, UserPrincipal userPrincipal) {
        ShiftResponse shiftResponse = new ShiftResponse(
            from.getId(),
            from.getSymbol()
        );

        return shiftResponse;
    }
}
