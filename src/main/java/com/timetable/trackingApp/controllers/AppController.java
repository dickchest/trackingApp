package com.timetable.trackingApp.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AppController {
    private FirebaseAuth firebaseAuth;

    @Autowired
    public AppController(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @GetMapping("/token")
    public Map<String, String> getToken() throws FirebaseAuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authortities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String customToken = firebaseAuth.createCustomToken("w.prajumsook", Collections.singletonMap("authorities", authortities));
        System.out.println("выполнил токен");
        return Collections.singletonMap("token", customToken);
    }
}
