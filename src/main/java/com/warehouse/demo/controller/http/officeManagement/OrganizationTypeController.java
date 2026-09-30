package com.warehouse.demo.controller.http.officeManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.mapper.employee.organizationType.OrganizationTypeResponseMapperImpl;
import com.warehouse.demo.service.employee.OrganizationTypeService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

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

@RestController
@RequestMapping("/organization_types")
@RequiredArgsConstructor
public class OrganizationTypeController {
    private final OrganizationTypeService organizationTypeService;
    private final OrganizationTypeResponseMapperImpl organizationTypeResponseMapperImpl;

    private final ControllerSecurity controllerSecurity;
    
    @GetMapping
    public ResponseEntity<List<? extends OrganizationTypeResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<OrganizationType> organizationTypes = organizationTypeService.readAll();
        List<? extends OrganizationTypeResponse> organizationTypeResponses = organizationTypes
            .stream()
            .map(ot -> returnObjectResponse(ot, userPrincipal))
            .toList();

        return new ResponseEntity<>(organizationTypeResponses, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<? extends OrganizationTypeResponse> readById(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        OrganizationType organizationType = organizationTypeService.read(id);
        OrganizationTypeResponse organizationTypeResponse = returnObjectResponse(organizationType, userPrincipal);

        return new ResponseEntity<>(organizationTypeResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends OrganizationTypeResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrganizationTypeRequest organizationTypeRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());

        OrganizationType organizationType = organizationTypeService.create(organizationTypeRequest);
        OrganizationTypeResponse organizationTypeResponse = returnObjectResponse(organizationType, userPrincipal);

        return new ResponseEntity<>(organizationTypeResponse, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<? extends OrganizationTypeResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrganizationTypeRequest organizationTypeRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());

        OrganizationType organizationType = organizationTypeService.update(id, organizationTypeRequest);
        OrganizationTypeResponse organizationTypeResponse = returnObjectResponse(organizationType, userPrincipal);

        return new ResponseEntity<>(organizationTypeResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        organizationTypeService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.ORGANIZATION_TYPE, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private OrganizationTypeResponse returnObjectResponse(OrganizationType from, UserPrincipal principal) {
        OrganizationTypeResponse response = organizationTypeResponseMapperImpl.convertToResponse(from);
        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
