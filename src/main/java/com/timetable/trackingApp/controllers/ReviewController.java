package com.timetable.trackingApp.controllers;

import com.timetable.trackingApp.domain.Reviews;
import com.timetable.trackingApp.services.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {
    private ReviewService service;

    @GetMapping("/getAll")
    public ResponseEntity<List<Reviews>> getAll() throws InterruptedException, ExecutionException {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Reviews entity, Principal principal) {
        return new ResponseEntity<>(service.create(entity, principal), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<Reviews> get(@RequestParam String documentId) throws InterruptedException, ExecutionException {
        return new ResponseEntity<>(service.get(documentId), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Reviews entity, Principal principal) throws InterruptedException, ExecutionException {
        return new ResponseEntity<>(service.update(entity, principal), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String documentId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(service.delete(documentId), HttpStatus.NO_CONTENT);
    }


}
