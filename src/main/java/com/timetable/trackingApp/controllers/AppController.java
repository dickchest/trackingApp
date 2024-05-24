package com.timetable.trackingApp.controllers;

import com.google.firebase.auth.FirebaseAuthException;
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
    public String getPrincipalName(Principal principal) throws FirebaseAuthException {
        return authService.getUserName(principal);
    }
}
