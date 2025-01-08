package com.example.main.service;

import java.util.List;

import com.example.main.DataModel.GameStateDTO;
import com.example.main.model.Task;

public interface SolutionService 
{
    List<GameStateDTO> checkSequence(Long idStudent, Long idTask);

    List<Task> findTaskBySolutionId(Long idSolution);

    Task randomGenerateTask(int rows, int cols, int numPotions, Long idTeacher);
}

