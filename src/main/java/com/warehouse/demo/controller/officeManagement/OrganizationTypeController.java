package com.warehouse.demo.controller.officeManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.mapper.employee.organizationType.OrganizationTypeResponseMapperImpl;
import com.warehouse.demo.service.employee.OrganizationTypeService;

import lombok.RequiredArgsConstructor;

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

@RestController
@RequestMapping("/organization_types")
@RequiredArgsConstructor
public class OrganizationTypeController {
    private final OrganizationTypeService organizationTypeService;
    private final OrganizationTypeResponseMapperImpl organizationTypeResponseMapperImpl;

    private final static String READ_ACCESS_ROLES =
        "hasAnyRole('DIRECTOR', 'MAJOR_HR', 'WAREHOUSE_EMPLOYEES_HR', " +
        "'OFFICE_EMPLOYEES_HR', 'ORDERS_PROCEEDER', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private final static String FULL_ACCESS_ROLES =
        "hasAnyRole('MAJOR_HR', 'SYSTEM_ADMINISTRATOR')";
    
    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends OrganizationTypeResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<OrganizationType> organizationTypes = organizationTypeService.readAll();
        List<? extends OrganizationTypeResponse> organizationTypeResponses = organizationTypes
            .stream()
            .map(ot -> returnObjectResponse(ot, userPrincipal))
            .toList();

        ResponseEntity<List<? extends OrganizationTypeResponse>> response = new ResponseEntity<>(organizationTypeResponses, HttpStatus.OK);
        return response;
    }
    
    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends OrganizationTypeResponse> readById(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        OrganizationType organizationType = organizationTypeService.read(id);
        OrganizationTypeResponse organizationTypeResponse = returnObjectResponse(organizationType, userPrincipal);

        ResponseEntity<OrganizationTypeResponse> response = new ResponseEntity<>(organizationTypeResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends OrganizationTypeResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrganizationTypeRequest organizationTypeRequest) {
        OrganizationType organizationType = organizationTypeService.create(organizationTypeRequest);
        OrganizationTypeResponse organizationTypeResponse = returnObjectResponse(organizationType, userPrincipal);

        ResponseEntity<OrganizationTypeResponse> response = new ResponseEntity<>(organizationTypeResponse, HttpStatus.CREATED);
        return response;
    }
    
    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends OrganizationTypeResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrganizationTypeRequest organizationTypeRequest) {
        OrganizationType organizationType = organizationTypeService.update(id, organizationTypeRequest);
        OrganizationTypeResponse organizationTypeResponse = returnObjectResponse(organizationType, userPrincipal);

        ResponseEntity<OrganizationTypeResponse> response = new ResponseEntity<>(organizationTypeResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        organizationTypeService.delete(id);

        ResponseEntity<String> response = new ResponseEntity<>("Organization type is deleted.", HttpStatus.OK);
        return response;
    }

    private OrganizationTypeResponse returnObjectResponse(OrganizationType from, UserPrincipal principal) {
        OrganizationTypeResponse response = organizationTypeResponseMapperImpl.convertToResponse(from);
        return response;
    }
}
