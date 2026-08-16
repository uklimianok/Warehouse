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

import com.warehouse.demo.dto.service.status.StatusRequest;
import com.warehouse.demo.dto.service.status.StatusResponse;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.warehouseService.StatusService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends StatusResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Status> statuses = statusService.readAll();
        List<? extends StatusResponse> statusResponse = statuses
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        ResponseEntity<List<? extends StatusResponse>> response = new ResponseEntity<>(statusResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends StatusResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Status status = statusService.read(id);
        StatusResponse statusResponse = returnObjectResponse(status, userPrincipal);

        ResponseEntity<StatusResponse> response = new ResponseEntity<>(statusResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends StatusResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody StatusRequest statusRequest) {
        Status status = statusService.create(statusRequest);
        StatusResponse statusResponse = returnObjectResponse(status, userPrincipal);

        ResponseEntity<StatusResponse> response = new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends StatusResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody StatusRequest statusRequest) {
        Status status = statusService.update(id, statusRequest);
        StatusResponse statusResponse = returnObjectResponse(status, userPrincipal);

        ResponseEntity<StatusResponse> response = new ResponseEntity<>(statusResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        statusService.delete(id);
        String message = Utility.getOutputMessage(EntityName.STATUS, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private StatusResponse returnObjectResponse(Status from, UserPrincipal userPrincipal) {
        StatusResponse statusResponse = new StatusResponse(
            from.getId(),
            from.getName(),
            from.getType()
        );

        return statusResponse;
    }
}
