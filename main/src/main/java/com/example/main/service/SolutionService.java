package com.example.main.service;

import java.util.List;

import com.example.main.model.Task;

public interface SolutionService 
{
    Boolean checkSequence(Long idStudent, Long idTask);

    List<Task> findTaskBySolutionId(Long idSolution);
}

