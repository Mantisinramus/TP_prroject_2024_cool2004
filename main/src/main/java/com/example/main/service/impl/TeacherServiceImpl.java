package com.example.main.service.impl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.model.Cell;
import com.example.main.model.Playground;
import com.example.main.model.Student;
import com.example.main.model.Task;
import com.example.main.repos.CellRepository;
import com.example.main.repos.PlaygroundRepository;
import com.example.main.repos.StudentRepository;
import com.example.main.repos.TaskRepository;
import com.example.main.repos.TeacherRepository;
import com.example.main.service.TeacherService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Primary
public class TeacherServiceImpl implements TeacherService {
    
    //Необходимые поля
    private final TeacherRepository repository;

    private final StudentRepository reposStudent;

    private final TaskRepository reposTask;

    private final PlaygroundRepository reposPlay;

    private final CellRepository reposCell;


    //Работа со студентом

    @Override
    public void addStudent(Student student)
    {
        reposStudent.save(student);

    }

    @Override
    public void deleteStudentById(Long idStudent) 
    {
        reposStudent.deleteById(idStudent);
    }

    @Override
    public Student findStudent(Long idStudent)
    {
        return reposStudent.findById(idStudent).get();
    }

    @Override
    public List<Student> findAllStudents() 
    {
        return reposStudent.findAll();
    }
    
    @Override
    public void changePassStudent(Long idStudent, String password) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        student.setStudentPassword(password);
    }
    

    //Работа с тестом

    @Override
    public void addTestStudent(Long idStudent, Long idTask) {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        student.addTask(idTask);
    }
    
    @Override
    public void deleteTestStudent(Long idStudent, Long idTask) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        student.deleteTask(idTask);
    }
    
    @Override
    public Task getTask(Long idTask) 
    {
        reposStudent.
        //return reposTask.findById(idTask).get();
    }


    








    @Override
    public List<Task> getAllTasks()
    {
        return reposTask.findAll();
    }
    
    @Override
    public void addTask(Playground playground, Task task) 
    {
        reposPlay.save(playground);
        reposTask.save(task);
    }


    //TODO жёстко эту хуйню придумать как решить это полный пиздец, который требует дурки тому кто придумал, ААХАПХАХП

    @Override
    public void editTask(Long idPlay, Long idTask, Playground editPlayground, Task editTask) 
    {
        Playground play = reposPlay.findById(idPlay).orElseThrow();
        Task task = reposTask.findById(idTask).orElseThrow();
        
        
        play.setSizeX(editPlayground.getSizeX());
        play.setSizeY(editPlayground.getSizeY());

        task.setTaskName(editTask.getTaskName());
        task.setTaskText(editTask.getTaskText());
        
    }

    @Override
    public void deleteTask(Long idTask) 
    {

    }

    
    //TODO смена фио учителю
    
    
    
    
    
    
    public void auth(String log, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'auth'");
    }

    @Override
    public void changePassword(String oldpassword, String newpassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }


   
   
    

    



    










}
