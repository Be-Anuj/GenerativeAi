package com.gen.ai.basics.controller;

import com.gen.ai.basics.service.ReplyGeneratingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/genai")
public class Controller {

    private final ReplyGeneratingService service;

    @PostMapping("/question")
    public ResponseEntity<List<String>> askAI(@RequestParam(name = "query") String query) {
        return ResponseEntity.ok(service.answerQuery(query));
    }
}
