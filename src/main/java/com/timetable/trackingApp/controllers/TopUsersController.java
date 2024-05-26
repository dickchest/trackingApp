package com.timetable.trackingApp.controllers;

import com.timetable.trackingApp.domain.TopUsers;
import com.timetable.trackingApp.services.TopUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/top_users")
@AllArgsConstructor
public class TopUsersController {
    private TopUserService service;

    @GetMapping("/getAll")
    public ResponseEntity<List<TopUsers>> getAll() throws InterruptedException, ExecutionException {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/top10")
    public void top() throws InterruptedException, ExecutionException {
        service.deleteUsersExceptTopTen();
    }

    @GetMapping("/get")
    public TopUsers get(@RequestParam String documentId) {
        return service.get(documentId);
    }
}
