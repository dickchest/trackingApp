package com.timetable.trackingApp.controllers;

import com.timetable.trackingApp.services.FirebaseAuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AppController {
    private FirebaseAuthService authService;

    @GetMapping("/getName")
    public String getPrincipalName(Principal principal) {
        return authService.getUserName(principal);
    }

    @GetMapping("/getEmail")
    public String getPrincipalEmail(Principal principal) {
        return authService.getUserEmail(principal);
    }
}
