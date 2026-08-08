package com.warehouse.demo.controller.officeManagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<? extends ShiftResponse>> readAll() {
        List<Shift> shifts = shiftService.readAll();
        List<ShiftResponse> shiftResponses = shifts
            .stream()
            .map(s -> new ShiftResponse(s.getId(), s.getSymbol()))
            .toList();

        ResponseEntity<List<? extends ShiftResponse>> response = new ResponseEntity<>(shiftResponses, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<ShiftResponse> read(@PathVariable long id) {
        Shift shift = shiftService.read(id);
        ShiftResponse shiftResponse = new ShiftResponse(shift.getId(), shift.getSymbol());

        ResponseEntity<ShiftResponse> response = new ResponseEntity<>(shiftResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<ShiftResponse> create(@RequestBody ShiftRequest shiftRequest) {
        Shift shift = shiftService.create(shiftRequest);
        ShiftResponse shiftResponse = new ShiftResponse(shift.getId(), shift.getSymbol());

        ResponseEntity<ShiftResponse> response = new ResponseEntity<>(shiftResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<ShiftResponse> update(@PathVariable long id, @RequestBody ShiftRequest shiftRequest) {
        Shift shift = shiftService.update(id, shiftRequest);
        ShiftResponse shiftResponse = new ShiftResponse(shift.getId(), shift.getSymbol());

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
}
