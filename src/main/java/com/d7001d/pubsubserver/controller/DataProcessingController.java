package com.d7001d.pubsubserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class DataProcessingController {

    @PostMapping("/pub")
    public ResponseEntity<String> createNewMessage(@RequestBody List<Integer> chunks) {
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/sub")
    public ResponseEntity<String> getMessage(@RequestParam Integer id) {
        return ResponseEntity.ok("Hello World");
    }
}
