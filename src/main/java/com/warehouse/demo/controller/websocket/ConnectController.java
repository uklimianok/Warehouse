package com.warehouse.demo.controller.websocket;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.warehouse.demo.service.employee.EmployeeService;

import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class ConnectController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final EmployeeService employeeService;

    @MessageMapping("/connect")
    public void connect(String message, Principal principal) {
        String employeeNumber = principal.getName();
        String fullMessage = employeeNumber + " " + message;

        simpMessagingTemplate.convertAndSend(
            "/topic/connect",
            fullMessage
        );

        employeeService.sendPendingNotifications(employeeNumber);
    }
}
