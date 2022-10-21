package com.d7001d.pubsubserver.controller;

import com.d7001d.pubsubserver.repository.DataRepository;
import com.d7001d.pubsubserver.service.DataProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DataProcessingController {

    private final DataProcessingService dataProcessingService;

    @PostMapping("/pub")
    public ResponseEntity<Instant> createNewMessage(@RequestBody List<Integer> chunk) {
        return ResponseEntity.ok(dataProcessingService.saveChunk(chunk).getCreatedAt());
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeAllData() {
        dataProcessingService.deleteAllData();
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/sub")
    public ResponseEntity<List<List<Integer>>> getMessage(@RequestParam List<Integer> id) {
        List<List<Integer>> foundTrunks = dataProcessingService.findTrunksForMessageByKey(id);
        if (foundTrunks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundTrunks);
    }

}

