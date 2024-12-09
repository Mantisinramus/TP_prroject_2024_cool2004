package com.example.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.service.SolutionService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/solution")
@AllArgsConstructor
public class SolutionController 
{
    private final SolutionService solutionService;

    @GetMapping("/checkSequence/{idStudent}/{idTask}")
    public ResponseEntity<Boolean> checkSequence(@PathVariable Long idStudent, @PathVariable Long idTask) {
        boolean isCorrect = solutionService.checkSequence(idStudent, idTask);
        if (isCorrect) return ResponseEntity.ok("Task completed successfully!");
    }

}
