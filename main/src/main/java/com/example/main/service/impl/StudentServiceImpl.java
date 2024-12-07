package com.example.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.model.SequenceOfPrimitives;
import com.example.main.model.Solution;
import com.example.main.model.Student;
import com.example.main.model.Task;
import com.example.main.repos.CellRepository;
import com.example.main.repos.PlaygroundRepository;
import com.example.main.repos.SequnceRepository;
import com.example.main.repos.StudentRepository;
import com.example.main.repos.TaskRepository;
import com.example.main.repos.TeacherRepository;
import com.example.main.service.StudentService;
import com.example.main.service.TeacherService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Primary
public class StudentServiceImpl implements StudentService
{   
     //Необходимые поля
    private final StudentRepository reposStudent;

    private final TaskRepository reposTask;

    private final SequnceRepository reposSequnce;
    
    //TODO пока не ебу как это делать
    
    @Override
    public void auth(String log, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'auth'");
    }

    //Работа с решением





    @Override
    public Task getTask(Long idTask) 
    {
        return reposTask.findById(idTask).get();
    }

    @Override
    public List<Task> getAllTasks(Long idStudent) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        List<Task> searchTask = new ArrayList();
        for (long idTask : student.getStudentTaskList()) 
        {
            searchTask.add(reposTask.findById(idStudent).get());
        }
        return searchTask;
    }

    @Override
    public void postAnswerTask(Long idTask, String AnswerTask) 
    {
        SequenceOfPrimitives sequence = reposSequnce.findById(idTask).orElseThrow();
        sequence.setSequenceText(AnswerTask);
    }

    @Override
    public List<Solution> getAllScore(Long idStudent) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        return;
    }

    @Override
    public Integer getScore(Long idStudent, Long idTask) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        
        return;
    }

}