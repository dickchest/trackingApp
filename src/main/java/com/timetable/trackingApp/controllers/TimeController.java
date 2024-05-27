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
import java.util.Map;
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
    public ResponseEntity<String> create(@RequestBody TimeEntriesDto dto, Principal principal) {
        return new ResponseEntity<>(service.create(dto, principal), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<TimeEntriesDto> get(@RequestParam String documentId) throws InterruptedException, ExecutionException {
        return new ResponseEntity<>(TimeConverter.toDto(service.get(documentId)), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody TimeEntriesDto dto, Principal principal) throws InterruptedException, ExecutionException {
        return new ResponseEntity<>(service.update(dto, principal), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String documentId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(service.delete(documentId), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/report")
    public Map<Integer, Map<String, List<TimeEntries>>> reportByWeeksAndCategory() throws InterruptedException, ExecutionException {
        return service.reportByWeeks();
    }

}
