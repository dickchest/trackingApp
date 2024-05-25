package com.timetable.trackingApp.controllers;

import com.timetable.trackingApp.services.FirebaseAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AppController {
    private FirebaseAuthService authService;

    @GetMapping("/getUid")
    public String getPrincipalName(Principal principal) {
        return authService.getUserUid(principal);
    }

    @GetMapping("/getEmail")
    public String getPrincipalEmail(Principal principal) {
        return authService.getUserEmail(principal);
    }

    @GetMapping("/getAllUid")
    public ResponseEntity<List<String>> getAll(){
        return new ResponseEntity<>(authService.getAll(), HttpStatus.OK);
    }
}
