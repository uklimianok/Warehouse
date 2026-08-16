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

import com.warehouse.demo.dto.employee.organization.FullOrganizationResponse;
import com.warehouse.demo.dto.employee.organization.OrganizationRequest;
import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.employee.OrganizationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('COORDINATOR', 'DATA_CONTROLLER', 'DIRECTOR', " +
        "'MAJOR_HR', 'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', " +
        "'ORDERS_PROCEEDER', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('MAJOR_HR', 'SYSTEM_ADMINISTRATOR')";

    private static final String[] FULL_ACCESS_ROLES_ARR =
        {"MAJOR_HR", "SYSTEM_ADMINISTRATOR"};

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends OrganizationResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Organization> organizations = organizationService.readAll();
        List<? extends OrganizationResponse> organizationResponses = organizations
            .stream()
            .map(o -> returnObjectResponse(o, userPrincipal))
            .toList();

        ResponseEntity<List<? extends OrganizationResponse>> response = new ResponseEntity<>(organizationResponses, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends OrganizationResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Organization organization = organizationService.read(id);
        OrganizationResponse organizationResponse = returnObjectResponse(organization, userPrincipal);

        ResponseEntity<OrganizationResponse> response = new ResponseEntity<>(organizationResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends OrganizationResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrganizationRequest organizationRequest) {
        Organization organization = organizationService.create(organizationRequest);
        OrganizationResponse organizationResponse = returnObjectResponse(organization, userPrincipal);
        
        ResponseEntity<OrganizationResponse> response = new ResponseEntity<>(organizationResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends OrganizationResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrganizationRequest organizationRequest) {
        Organization organization = organizationService.update(id, organizationRequest);
        OrganizationResponse organizationResponse = returnObjectResponse(organization, userPrincipal);

        ResponseEntity<OrganizationResponse> response = new ResponseEntity<>(organizationResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        organizationService.delete(id);

        ResponseEntity<String> response = new ResponseEntity<>("Organization deleted.", HttpStatus.OK);
        return response;
    }

    private OrganizationResponse returnObjectResponse(Organization from, UserPrincipal userPrincipal) {
        return userPrincipal.hasAnyRole(FULL_ACCESS_ROLES_ARR) ?
            new FullOrganizationResponse(
                from.getId(),
                from.getName(),
                from.getOrganizationNumber(),
                new OrganizationTypeResponse(from.getOrganizationType().getId(), from.getOrganizationType().getName()),
                from.getAddress(),
                from.getPhoneNumber(),
                from.getEmail(),
                from.getUrl()
            ) :
            new OrganizationResponse(
                from.getId(),
                from.getName(),
                from.getOrganizationNumber()
            );
    }
}
