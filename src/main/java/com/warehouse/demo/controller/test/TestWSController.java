package com.warehouse.demo.controller.test;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class TestWSController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/greet")
    public void greet(String message) {
        simpMessagingTemplate.convertAndSend("/topic/test", message);
    }

    @MessageMapping("/greet-private")
    public void greetPrivate(String message, Principal principal) {
        String fullMessage = "User: " + principal.getName() + ". " + message;
        simpMessagingTemplate.convertAndSendToUser(
            principal.getName(),    // Contains Employee.employeeNumber value
            "/queue/test", 
            fullMessage
        );
    }
}
