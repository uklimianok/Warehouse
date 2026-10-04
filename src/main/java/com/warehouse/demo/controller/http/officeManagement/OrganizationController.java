package com.warehouse.demo.controller.http.officeManagement;

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

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.organization.OrganizationRequest;
import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.mapper.employee.organization.OrganizationResponseMapper;
import com.warehouse.demo.service.employee.OrganizationService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;
    private final OrganizationResponseMapper organizationResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends OrganizationResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Organization> organizations = organizationService.readAll();
        List<? extends OrganizationResponse> organizationResponses = organizations
            .stream()
            .map(o -> returnObjectResponse(o, userPrincipal))
            .toList();

        return new ResponseEntity<>(organizationResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends OrganizationResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        Organization organization = organizationService.read(id);
        OrganizationResponse organizationResponse = returnObjectResponse(organization, userPrincipal);

        return new ResponseEntity<>(organizationResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends OrganizationResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid OrganizationRequest organizationRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Organization organization = organizationService.create(organizationRequest);
        OrganizationResponse organizationResponse = returnObjectResponse(organization, userPrincipal);
        
        return new ResponseEntity<>(organizationResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends OrganizationResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid OrganizationRequest organizationRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Organization organization = organizationService.update(id, organizationRequest);
        OrganizationResponse organizationResponse = returnObjectResponse(organization, userPrincipal);

        return new ResponseEntity<>(organizationResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        organizationService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.ORGANIZATION, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private OrganizationResponse returnObjectResponse(
        Organization from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> organizationResponseMapper.convertToFullResponse(from);
            default -> organizationResponseMapper.convertToResponse(from);
        };
    }  
}
