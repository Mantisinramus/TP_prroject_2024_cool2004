package com.example.main.service;

import java.util.List;

import javax.sound.midi.Sequence;

import com.example.main.model.Playground;
import com.example.main.model.Student;
import com.example.main.model.Task;

public interface TeacherService 
{
    //вход

    void auth(String log, String password);

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

    void addTask(Playground playground, Task task);

    void deleteTask(Long idTask);

    void editTask(Long idPlay, Long idTask, Playground playground, Task task);

    //Журнал пока хз, надеюсь сегодня придумаю что-нибудь пжжжж ГОСПОДИ ПОМОГИ МНЕ ЗАЩИТИ ГРЕШНЫЕ ДУШИ

    Integer getMarkByStydentByTask(Long idStudent, Long idTask);

    List<Integer> getMarksByStydent(Long idStudent);

    //List<> getMarksByStydents(); - не могу придумать

    void setMarkbyStydent(Long idStudent, Long idTask, Integer mark);

    //Работа с решениеми

    Sequence getSequenceStydent(Long idStudent, Integer Task);

    Boolean checkSequence(Sequence checkSequence);

    void getAnswerBySequence(String Answer);

}

