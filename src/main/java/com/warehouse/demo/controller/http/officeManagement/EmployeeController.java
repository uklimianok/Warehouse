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
import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.dto.employee.EmployeeResponse;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.mapper.employee.EmployeeResponseMapper;
import com.warehouse.demo.service.employee.EmployeeService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeResponseMapper employeeResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends EmployeeResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        String responseType = controllerSecurity.getResponseObjectType(getClass(), userPrincipal.getEmployeeNumber());

        List<Employee> employees = employeeService.readAll();
        List<? extends EmployeeResponse> employeeResponse = employees
            .stream()
            .map(e -> {
                return switch (responseType) {
                    case "Full" -> employeeResponseMapper.convertToFullResponse(e);
                    case "DataController" -> employeeResponseMapper.convertToDataControllerResponse(e);
                    default -> employeeResponseMapper.convertToResponse(e);
                };
            })
            .toList();
        
        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends EmployeeResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        Employee employee = employeeService.read(id);
        EmployeeResponse employeeResponse = returnObjectResponse(employee, userPrincipal);

        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends EmployeeResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid EmployeeRequest employeeRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Employee employee = employeeService.create(employeeRequest);
        EmployeeResponse employeeResponse = returnObjectResponse(employee, userPrincipal);

        return new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends EmployeeResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid EmployeeRequest employeeRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Employee employee = employeeService.update(id, employeeRequest, userPrincipal);
        EmployeeResponse employeeResponse = returnObjectResponse(employee, userPrincipal);

        return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        employeeService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private EmployeeResponse returnObjectResponse(
        Employee from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> employeeResponseMapper.convertToFullResponse(from);
            case "DataController" -> employeeResponseMapper.convertToDataControllerResponse(from);
            default -> employeeResponseMapper.convertToResponse(from);
        };
    }
}
