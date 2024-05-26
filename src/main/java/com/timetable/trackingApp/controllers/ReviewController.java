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
    public String create(@RequestBody Reviews entity, Principal principal) {
        return service.create(entity, principal);
    }

    @GetMapping("/get")
    public Reviews get(@RequestParam String documentId) throws InterruptedException, ExecutionException {
        return service.get(documentId);
    }

    @PutMapping("/update")
    public String update(@RequestBody Reviews entity, Principal principal) throws InterruptedException, ExecutionException {
        return service.update(entity, principal);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String documentId) throws ExecutionException, InterruptedException {
        return service.delete(documentId);
    }


}
