package com.warehouse.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.security.UserPrincipal;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {
    @GetMapping("/whoami")
    public Map<String, Object> getWhoAmI(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> response = Map.of(
            "username", userPrincipal.getUser().getEmployee().getEmployeeNumber(),
            "role", userPrincipal.getAuthorities(),
            "isEnabled", userPrincipal.isEnabled(),
            "message", "Congratulations! You managed to receive this message."
        );
        return response;
    }
}
