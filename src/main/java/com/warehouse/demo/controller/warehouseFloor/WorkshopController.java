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

import com.warehouse.demo.dto.workplace.workshop.WorkshopRequest;
import com.warehouse.demo.dto.workplace.workshop.WorkshopResponse;
import com.warehouse.demo.entity.workplace.Workshop;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.workplace.WorkshopService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class WorkshopController {
    private final WorkshopService workshopService;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('DATA_CONTROLLER', 'DIRECTOR'," +
        "'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends WorkshopResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Workshop> workshops = workshopService.readAll();
        List<? extends WorkshopResponse> workshopResponse = workshops
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        ResponseEntity<List<? extends WorkshopResponse>> response = new ResponseEntity<>(workshopResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends WorkshopResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Workshop workshop = workshopService.read(id);
        WorkshopResponse workshopResponse = returnObjectResponse(workshop, userPrincipal);

        ResponseEntity<WorkshopResponse> response = new ResponseEntity<>(workshopResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends WorkshopResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody WorkshopRequest workshopRequest) {
        Workshop workshop = workshopService.create(workshopRequest);
        WorkshopResponse workshopResponse = returnObjectResponse(workshop, userPrincipal);

        ResponseEntity<WorkshopResponse> response = new ResponseEntity<>(workshopResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends WorkshopResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody WorkshopRequest workshopRequest) {
        Workshop workshop = workshopService.update(id, workshopRequest);
        WorkshopResponse workshopResponse = returnObjectResponse(workshop, userPrincipal);

        ResponseEntity<WorkshopResponse> response = new ResponseEntity<>(workshopResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        workshopService.delete(id);
        String message = Utility.getOutputMessage(EntityName.WORKSHOP, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private WorkshopResponse returnObjectResponse(Workshop from, UserPrincipal userPrincipal) {
        WorkshopResponse workshopResponse = new WorkshopResponse(
            from.getId(),
            from.getName(),
            from.getStandard()
        );

        return workshopResponse;
    }
}
