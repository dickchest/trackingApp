package com.timetable.trackingApp.controllers;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserRecord;
import com.timetable.trackingApp.domain.UserDetails;
import com.timetable.trackingApp.services.FirebaseAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<String>> getAll() {
        return new ResponseEntity<>(authService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public String createUser(@RequestBody UserDetails user) throws FirebaseAuthException {
        return authService.create(user);
    }

    @PutMapping("/update")
    public String updateUser(@RequestBody UserDetails user) throws FirebaseAuthException {
        return authService.update(user);
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserInfo[]> getUser(@RequestParam String documentId) {
        return new ResponseEntity<>(authService.getUser(documentId), HttpStatus.OK);
    }
}
