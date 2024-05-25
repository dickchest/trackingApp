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
    public String createCategory(@RequestBody Categories category){
        return service.create(category);
    }

    @GetMapping("/get")
    public Categories get(@RequestParam String documentId){
        return service.get(documentId);
    }

    @PutMapping("/update")
    public String update(@RequestBody Categories category) throws ExecutionException, InterruptedException {
        return service.update(category);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String documentId){
        return service.delete(documentId);
    }
}
