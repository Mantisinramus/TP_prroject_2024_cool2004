package com.example.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.model.Solution;
import com.example.main.model.Student;
import com.example.main.model.Task;
import com.example.main.repos.SolutionRepository;
import com.example.main.repos.StudentRepository;
import com.example.main.repos.TaskRepository;
import com.example.main.repos.TeacherRepository;
import com.example.main.service.StudentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Primary
public class StudentServiceImpl implements StudentService
{   
     //Необходимые поля
    private final StudentRepository reposStudent;

    private final TaskRepository reposTask;

    private final SolutionRepository reposSolut;

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
    public Task getTask(Long idTask) 
    {
        return reposTask.findById(idTask).get();
    }

    @Override
    public List<Task> getAllTasks(Long idStudent) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        return reposTask.findAllById(student.getTasksId());
    }

    //положить решение
    @Override
    public void postAnswerTask(Long idStudent, Long idTask, String AnswerTask) 
    {
        Solution solution = reposSolut.findById(reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask)).orElseThrow();
        solution.setSequenceText(AnswerTask);
    }

    //получение оценки
    @Override
    public Integer getMark(Long idStudent, Long idTask) 
    {
        Solution solution = reposSolut.findById(reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask)).orElseThrow();
        return solution.getMark();
    }

    @Override
    public List<Integer> getAllMarks(Long idStudent) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();        
        List<Integer> marksStydent = new ArrayList<>();
        for (Long tasks : student.getTasksId()) 
        {
            Solution sol = reposSolut.findById(reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, tasks)).orElseThrow(() -> new RuntimeException("Student not found"));
            marksStydent.add(sol.getMark());
        }
        return marksStydent;
    }
}
