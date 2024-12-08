package com.example.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final TeacherRepository reposTeacher;
    
    //TODO пока не ебу как это делать
    
    @Override
    public Long auth(String log, String password) 
    {
        // Ищем ID студента по логину
        Long studentId = reposStudent.findStudentIdByLogin(log);
    
        // Если студент с таким логином не найден
        if (studentId == null) {
            // throw new StudentNotFoundException("Student not found with login: " + log);
        }   
    
        // Если студент найден, извлекаем его из базы
        Student student = reposStudent.findById(studentId).get();
    
        // Проверяем пароль
        if (student.getStudentPassword().equals(password)) {
            // Если пароль верный, возвращаем ID студента
            return studentId;
        } else {
            // Если пароль неверный, выбрасываем исключение
            //throw new IncorrectPasswordException("Incorrect password for student with login: " + log);
            return (long)0;
        }
        
    }

    //Работа с решением

    @Override
    public Task getTask(Long idStudent, Long idTask) 
    {
        return reposStudent.getTaskByStudentAndTaskId(idStudent, idTask);
    }

    @Override
    public List<Task> getAllTasks(Long idStudent) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        return student.getTasks();
    }

    @Override
    public void postAnswerTask(Long idStudent, Long idTask, String AnswerTask) 
    {
        SequenceOfPrimitives sequence = reposSequnce.findById(reposSequnce.findSolutionIdByStudentAndTask(idStudent, idTask)).orElseThrow();
        sequence.setSequenceText(AnswerTask);
    }

    @Override
    public Integer getMark(Long idStudent, Long idTask) 
    {
        return reposTeacher.findMarkByStudentAndTask(idStudent, idTask);
    }

    @Override
    public List<Integer> getAllMarks(Long idStudent) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();        
        List<Integer> marksStydent = new ArrayList<>();
        for (Task tasks : student.getTasks()) 
        {
            marksStydent.add(reposTeacher.findMarkByStudentAndTask(idStudent, tasks.getTaskId()));
        }
        return marksStydent;
    }



}
