package com.timetable.trackingApp.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AppController {

    @GetMapping("/getName")
    public String getPrincipalName(Principal principal) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(principal.getName());
        System.out.println("Successfully fetched user data: " + userRecord.getEmail() + userRecord);
        return principal.getName();
    }
}
