package com.warehouse.demo.controller.test;

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
    public void greetPrivate(String message) {
        simpMessagingTemplate.convertAndSendToUser("24000001", "/queue/test", message);
    }
}
