package com.example.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.model.Task;
import com.example.main.service.SolutionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/solution")
@AllArgsConstructor
public class SolutionController 
{
    private final SolutionService solutionService;

    @GetMapping("/checkSequence/{idStudent}/{idTask}")
    public ResponseEntity<Boolean> checkSequence(@PathVariable Long idStudent, @PathVariable Long idTask) 
    {
       return ResponseEntity.ok(solutionService.checkSequence(idStudent, idTask));
    }


    @GetMapping("/taskName/{idSolution}")
    public ResponseEntity<List<Task>> findTaskNameBySolutionId(@PathVariable Long idSolution) 
    {
       return ResponseEntity.ok(solutionService.findTaskBySolutionId(idSolution));
    }
   

    

}
