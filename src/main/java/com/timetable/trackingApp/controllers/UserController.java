package com.timetable.trackingApp.controllers;

import com.timetable.trackingApp.domain.UserDetails;
import com.timetable.trackingApp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/create")
    public String createUser(@RequestBody UserDetails user) throws InterruptedException, ExecutionException {
        return userService.createUser(user);
    }

    @GetMapping("/get")
    public UserDetails getUser(@RequestParam String documentId) throws InterruptedException, ExecutionException {
        return userService.getUser(documentId);
    }

    @PutMapping("/update")
    public String updateUser(@RequestBody UserDetails user) throws InterruptedException, ExecutionException {
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam String documentId) throws InterruptedException, ExecutionException {
        return userService.deleteUser(documentId);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint() {
        return ResponseEntity.ok("Test Get Endpoint is Working");
    }

}
