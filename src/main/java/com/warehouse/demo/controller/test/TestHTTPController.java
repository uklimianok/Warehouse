package com.warehouse.demo.controller.test;

import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestHTTPController {
    @GetMapping("/whoami")
    public Map<String, Object> getWhoAmI(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> response = Map.of(
            "username", userPrincipal.getName(),
            "role", userPrincipal.getAuthorities(),
            "message", "Congratulations! You managed to receive this message."
        );
        return response;
    }
}
