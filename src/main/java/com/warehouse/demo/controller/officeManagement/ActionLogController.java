package com.warehouse.demo.controller.officeManagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.service.actionLog.ActionLogResponse;
import com.warehouse.demo.entity.service.ActionLog;
import com.warehouse.demo.mapper.service.actionLog.ActionLogResponseMapper;
import com.warehouse.demo.service.warehouseService.ActionLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/action-logs")
@RequiredArgsConstructor
public class ActionLogController {
    private final ActionLogService actionLogService;
    private final ActionLogResponseMapper actionLogResponseMapper;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('COORDINATOR', 'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', " +
        "'STATISTICS_PROCEEDER', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends ActionLogResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ActionLog> actionLogs = actionLogService.readAll();
        List<? extends ActionLogResponse> actionLogsResponse = actionLogs
            .stream()
            .map(al -> returnObjectResponse(al, userPrincipal))
            .toList();

        ResponseEntity<List<? extends ActionLogResponse>> response = new ResponseEntity<>(actionLogsResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends ActionLogResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        ActionLog actionLog = actionLogService.read(id);
        ActionLogResponse actionLogResponse = returnObjectResponse(actionLog, userPrincipal);

        ResponseEntity<? extends ActionLogResponse> response = new ResponseEntity<>(actionLogResponse, HttpStatus.OK);
        return response;
    }

    private ActionLogResponse returnObjectResponse(ActionLog from, UserPrincipal principal) {
        ActionLogResponse response = actionLogResponseMapper.convertToResponse(from);
        return response;
    }
}
