package com.example.main.service;

import java.util.List;
import java.util.Optional;

import com.example.main.model.Solution;
import com.example.main.model.Task;

public interface StudentService 
{
    //Вход

    Long auth(String log, String password);

    //Работа с решением

    Task getTask(Long idTask);

    List<Task> getAllTasks(Long idStudent);

    void postAnswerTask(Long idStudent, Long idTask, String AnswerTask);

    //Журнал

    Integer getMark(Long idStudent, Long idTask);

    List<Integer> getAllMarks(Long idStudent);

    List<Solution> getAllSolutions(Long idStudent);

    Optional<Solution> getSolution(Long idSolution);

}
