package com.warehouse.demo.controller.http.officeManagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.department.DepartmentRequest;
import com.warehouse.demo.dto.employee.department.DepartmentResponse;
import com.warehouse.demo.entity.employee.Department;
import com.warehouse.demo.mapper.employee.department.DepartmentResponseMapper;
import com.warehouse.demo.service.employee.DepartmentService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/departments")
@RequiredArgsConstructor 
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentResponseMapper departmentResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping 
    public ResponseEntity<List<? extends DepartmentResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Department> departments = departmentService.readAll();
        List<? extends DepartmentResponse> departmentsResponse = departments
            .stream()
            .map(d -> returnObjectResponse(d, userPrincipal))
            .toList();

        return new ResponseEntity<>(departmentsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends DepartmentResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        Department department = departmentService.read(id);
        DepartmentResponse departmentResponse = returnObjectResponse(department, userPrincipal);

        return new ResponseEntity<>(departmentResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends DepartmentResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid DepartmentRequest departmentRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Department department = departmentService.create(departmentRequest);
        DepartmentResponse departmentResponse = returnObjectResponse(department, userPrincipal);

        return new ResponseEntity<>(departmentResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends DepartmentResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid DepartmentRequest departmentRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Department department = departmentService.update(id, departmentRequest);
        DepartmentResponse departmentResponse = returnObjectResponse(department, userPrincipal);

        return new ResponseEntity<>(departmentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        departmentService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.DEPARTMENT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private DepartmentResponse returnObjectResponse(
        Department from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case ("Full") -> departmentResponseMapper.convertToFullResponse(from);
            default -> departmentResponseMapper.convertToResponse(from);
        };
    }
}
