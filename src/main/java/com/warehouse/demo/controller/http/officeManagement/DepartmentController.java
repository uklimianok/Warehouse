package com.warehouse.demo.controller.http.officeManagement;

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

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.department.DepartmentRequest;
import com.warehouse.demo.dto.employee.department.DepartmentResponse;
import com.warehouse.demo.entity.employee.Department;
import com.warehouse.demo.mapper.employee.department.DepartmentResponseMapper;
import com.warehouse.demo.service.employee.DepartmentService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/departments")
@RequiredArgsConstructor 
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentResponseMapper departmentResponseMapper;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('GOODS_UNLOADER', 'GOODS_PICKER', 'SET_GOODS_EXPORTER', 'SET_GOODS_LOADER', " +
        "'OPERATOR', 'RETURN_GOODS_CONTROLLER', 'COORDINATOR', 'DATA_CONTROLLER', " +
        "'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', 'DIRECTOR', 'MAJOR_HR', " +
        "'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES =
        "hasAnyRole('MAJOR_HR', 'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('MAJOR_HR', 'SYSTEM_ADMINISTRATOR')";

    private static final String[] FULL_ACCESS_ROLES_ARR = {"MAJOR_HR", "SYSTEM_ADMINISTRATOR"};

    @GetMapping 
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends DepartmentResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Department> departments = departmentService.readAll();
        List<? extends DepartmentResponse> departmentsResponse = departments
            .stream()
            .map(d -> returnObjectResponse(d, userPrincipal))
            .toList();

        ResponseEntity<List<? extends DepartmentResponse>> response = new ResponseEntity<>(departmentsResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends DepartmentResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Department department = departmentService.read(id);
        DepartmentResponse departmentResponse = returnObjectResponse(department, userPrincipal);

        ResponseEntity<? extends DepartmentResponse> response = new ResponseEntity<>(departmentResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends DepartmentResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody DepartmentRequest departmentRequest) {
        Department department = departmentService.create(departmentRequest);
        DepartmentResponse departmentResponse = returnObjectResponse(department, userPrincipal);

        ResponseEntity<? extends DepartmentResponse> response = new ResponseEntity<>(departmentResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends DepartmentResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody DepartmentRequest departmentRequest) {
        Department department = departmentService.update(id, departmentRequest);
        DepartmentResponse departmentResponse = returnObjectResponse(department, userPrincipal);

        ResponseEntity<? extends DepartmentResponse> response = new ResponseEntity<>(departmentResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        departmentService.delete(id);
        String message = Utility.getOutputMessage(Entity.DEPARTMENT, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private DepartmentResponse returnObjectResponse(Department from, UserPrincipal principal) {
        DepartmentResponse departmentResponse = null;
        if (principal.hasAnyRole(FULL_ACCESS_ROLES_ARR))
            departmentResponse = departmentResponseMapper.convertToFullResponse(from);
        else
            departmentResponse = departmentResponseMapper.convertToResponse(from);

        return departmentResponse;
    }
}
