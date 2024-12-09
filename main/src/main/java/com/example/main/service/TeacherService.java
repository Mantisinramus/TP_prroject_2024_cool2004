package com.example.main.service;

import java.util.List;

import com.example.main.model.Solution;
import com.example.main.model.Student;
import com.example.main.model.Task;
import com.example.main.model.Teacher;

public interface TeacherService 
{
    //простое и реализовано

    //регистрация

    void addTeacher(Teacher teacher);

    //вход

    Long auth(String log, String password);

    void changePassword(Long idTeacher, String newpassword);

    //Работа со студентом

    Long searchStudentInitial(String Initial);

    void addStudent(Student student);

    void deleteStudentById(Long idStudent);

    Student findStudent(Long idStudent);

    List<Student> findAllStudents();

    void changePassStudent(Long idStudent, String password);

    //Работа с тестом

    void addTestStudent(Long idStudent, Long idTask);

    void deleteTestStudent(Long idStudent, Long idTask);

    Task getTask(Long idTask);
    
    List<Task> getAllTasks();

    void addTask(Task task);

    void deleteTask(Long idTask);

    Integer getMarkByStydentByTask(Long idStudent, Long idTask);

    List<Integer> getMarksByStydent(Long idStudent);

    void setMarkbyStydent(Long idStudent, Long idTask, Integer mark);

    //Работа с решениеми

    
    void editTask(Long idTask, Task task);

    Solution getSolutionStydent(Long idStudent, Long idTask);

    void setAnswerBySequence(Long idStudent, Long idTask, String Answer);

}

