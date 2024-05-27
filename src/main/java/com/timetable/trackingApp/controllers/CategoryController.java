package com.timetable.trackingApp.controllers;

import com.timetable.trackingApp.domain.Categories;
import com.timetable.trackingApp.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private CategoryService service;

    @GetMapping("/getAll")
    public ResponseEntity<List<Categories>> getAll() throws InterruptedException, ExecutionException {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestBody Categories category) {
        return new ResponseEntity<>(service.create(category), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<Categories> get(@RequestParam String documentId) {
        return new ResponseEntity<>(service.get(documentId), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Categories category) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(service.update(category), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String documentId) {
        return new ResponseEntity<>(service.delete(documentId), HttpStatus.NO_CONTENT);
    }
}
