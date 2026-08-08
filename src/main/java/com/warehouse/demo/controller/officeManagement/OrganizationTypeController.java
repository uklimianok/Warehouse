package com.warehouse.demo.controller.officeManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.service.employee.OrganizationTypeService;

import lombok.RequiredArgsConstructor;

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

@RestController
@RequestMapping("/organization_types")
@RequiredArgsConstructor
public class OrganizationTypeController {
    private final OrganizationTypeService organizationTypeService;

    private final static String READ_ACCESS_ROLES =
        "hasAnyRole('DIRECTOR', 'MAJOR_HR', 'WAREHOUSE_EMPLOYEES_HR', " +
        "'OFFICE_EMPLOYEES_HR', 'ORDERS_PROCEEDER', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private final static String FULL_ACCESS_ROLES =
        "hasAnyRole('MAJOR_HR', 'SYSTEM_ADMINISTRATOR')";
    
    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends OrganizationTypeResponse>> readAll() {
        List<OrganizationType> organizationTypes = this.organizationTypeService.readAll();
        List<? extends OrganizationTypeResponse> organizationTypeResponses = organizationTypes
            .stream()
            .map(ot -> new OrganizationTypeResponse(ot.getId(), ot.getName()))
            .toList();

        ResponseEntity<List<? extends OrganizationTypeResponse>> response = new ResponseEntity<>(organizationTypeResponses, HttpStatus.OK);
        return response;
    }
    
    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<OrganizationTypeResponse> readById(@PathVariable long id) {
        OrganizationType organizationType = this.organizationTypeService.readById(id);
        OrganizationTypeResponse organizationTypeResponse = new OrganizationTypeResponse(
            organizationType.getId(), 
            organizationType.getName()
        );

        ResponseEntity<OrganizationTypeResponse> response = new ResponseEntity<>(organizationTypeResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<OrganizationTypeResponse> create(@RequestBody OrganizationTypeRequest organizationTypeRequest) {
        OrganizationType organizationType = this.organizationTypeService.create(organizationTypeRequest);
        OrganizationTypeResponse organizationTypeResponse = new OrganizationTypeResponse(
            organizationType.getId(), 
            organizationType.getName()
        );

        ResponseEntity<OrganizationTypeResponse> response = new ResponseEntity<>(organizationTypeResponse, HttpStatus.CREATED);
        return response;
    }
    
    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<OrganizationTypeResponse> update(@PathVariable long id, @RequestBody OrganizationTypeRequest organizationTypeRequest) {
        OrganizationType organizationType = this.organizationTypeService.update(id, organizationTypeRequest);
        OrganizationTypeResponse organizationTypeResponse = new OrganizationTypeResponse(
            organizationType.getId(),
            organizationType.getName()
        );

        ResponseEntity<OrganizationTypeResponse> response = new ResponseEntity<>(organizationTypeResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        this.organizationTypeService.deleteById(id);

        ResponseEntity<String> response = new ResponseEntity<>("Organization type is deleted.", HttpStatus.OK);
        return response;
    }
}
