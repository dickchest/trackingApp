package com.timetable.trackingApp.controllers;

import com.timetable.trackingApp.domain.TimeEntries;
import com.timetable.trackingApp.dto.TimeEntriesDto;
import com.timetable.trackingApp.services.TimeService;
import com.timetable.trackingApp.services.Utils.TimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/time")
@AllArgsConstructor
public class TimeController {
    private TimeService service;

    @GetMapping("/getAll")
    public ResponseEntity<List<TimeEntriesDto>> getAll() throws InterruptedException, ExecutionException {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public String create(@RequestBody TimeEntriesDto dto, Principal principal) {
        return service.create(dto, principal);
    }

    @GetMapping("/get")
    public TimeEntriesDto get(@RequestParam String documentId) throws InterruptedException, ExecutionException {
        return TimeConverter.toDto(service.get(documentId));
    }

    @PutMapping("/update")
    public String update(@RequestBody TimeEntriesDto dto, Principal principal) throws InterruptedException, ExecutionException {
        return service.update(dto, principal);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String documentId) throws ExecutionException, InterruptedException {
        return service.delete(documentId);
    }
}
