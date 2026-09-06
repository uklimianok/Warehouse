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

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.dto.employee.EmployeeResponse;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.mapper.employee.EmployeeResponseMapper;
import com.warehouse.demo.service.employee.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeResponseMapper employeeResponseMapper;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('GOODS_UNLOADER', 'GOODS_PICKER', 'SET_GOODS_EXPORTER', " +
        "'SET_GOODS_LOADER', 'OPERATOR', 'RETURN_GOODS_CONTROLLER', 'COORDINATOR', " +
        "'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', 'DIRECTOR', 'MAJOR_HR', " +
        "'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', 'STATISTICS_PROCEEDER', " +
        "'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES = 
        "hasAnyRole('COORDINATOR', 'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', " +
        "'MAJOR_HR', 'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('MAJOR_HR', 'WAREHOUSE_EMPLOYEES_HR', 'OFFICE_EMPLOYEES_HR', " +
        "'SYSTEM_ADMINISTRATOR')";

    private static final String[] READ_UPDATE_ACCESS_ROLES_ARR = 
        {
            "COORDINATOR", "DATA_CONTROLLER", "SHIFT_SUPERVISOR", 
            "MAJOR_HR", "WAREHOUSE_EMPLOYEES_HR", "OFFICE_EMPLOYEES_HR",
            "SYSTEM_ADMINISTRATOR"
        };
    private static final String[] FULL_ACCESS_ROLES_ARR = 
        {
            "MAJOR_HR", "WAREHOUSE_EMPLOYEES_HR", "OFFICE_EMPLOYEES_HR",
            "SYSTEM_ADMINISTRATOR"
        };

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends EmployeeResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Employee> employees = employeeService.readAll();
        List<? extends EmployeeResponse> employeeResponse = employees
            .stream()
            .map(e -> returnObjectResponse(e, userPrincipal))
            .toList();
        
        ResponseEntity<List<? extends EmployeeResponse>> response = new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends EmployeeResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Employee employee = employeeService.read(id);
        EmployeeResponse employeeResponse = returnObjectResponse(employee, userPrincipal);

        ResponseEntity<EmployeeResponse> response = new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends EmployeeResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeService.create(employeeRequest);
        EmployeeResponse employeeResponse = returnObjectResponse(employee, userPrincipal);

        ResponseEntity<EmployeeResponse> response = new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends EmployeeResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeService.update(id, employeeRequest);
        EmployeeResponse employeeResponse = returnObjectResponse(employee, userPrincipal);

        ResponseEntity<EmployeeResponse> response = new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        employeeService.delete(id);

        ResponseEntity<String> response = new ResponseEntity<>("Employee deleted.", HttpStatus.OK);
        return response;
    }

    private EmployeeResponse returnObjectResponse(Employee from, UserPrincipal principal) {
        EmployeeResponse response = null;
        if (principal.hasAnyRole(FULL_ACCESS_ROLES_ARR))
            response = employeeResponseMapper.convertToFullResponse(from);
        else if (principal.hasAnyRole(READ_UPDATE_ACCESS_ROLES_ARR))
            response = employeeResponseMapper.convertToDataControllerResponse(from);
        else
            response = employeeResponseMapper.convertToResponse(from);

        return response;
    }
}
