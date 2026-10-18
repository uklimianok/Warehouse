package local.warehouse.app.controller.http.officeManagement;

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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import local.warehouse.app.configuration.security.UserPrincipal;
import local.warehouse.app.dto.employee.organization.OrganizationRequest;
import local.warehouse.app.dto.employee.organization.OrganizationResponse;
import local.warehouse.app.entity.employee.Organization;
import local.warehouse.app.mapper.employee.organization.OrganizationResponseMapper;
import local.warehouse.app.service.employee.OrganizationService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
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
