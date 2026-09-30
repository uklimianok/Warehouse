package com.warehouse.demo.controller.http.officeManagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/action-logs")
@RequiredArgsConstructor
public class ActionLogController {
    private final ActionLogService actionLogService;
    private final ActionLogResponseMapper actionLogResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends ActionLogResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<ActionLog> actionLogs = actionLogService.readAll();
        List<? extends ActionLogResponse> actionLogsResponse = actionLogs
            .stream()
            .map(al -> returnObjectResponse(al, userPrincipal))
            .toList();

        return new ResponseEntity<>(actionLogsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ActionLogResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        ActionLog actionLog = actionLogService.read(id);
        ActionLogResponse actionLogResponse = returnObjectResponse(actionLog, userPrincipal);

        return new ResponseEntity<>(actionLogResponse, HttpStatus.OK);
    }

    private ActionLogResponse returnObjectResponse(ActionLog from, UserPrincipal principal) {
        ActionLogResponse response = actionLogResponseMapper.convertToResponse(from);
        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
