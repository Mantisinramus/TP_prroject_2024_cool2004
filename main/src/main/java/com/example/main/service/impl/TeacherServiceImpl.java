package com.example.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.Sequence;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.model.Cell;
import com.example.main.model.Playground;
import com.example.main.model.Student;
import com.example.main.model.Task;
import com.example.main.model.Teacher;
import com.example.main.model.Solution;
import com.example.main.repos.CellRepository;
import com.example.main.repos.PlaygroundRepository;
import com.example.main.repos.SolutionRepository;
import com.example.main.repos.StudentRepository;
import com.example.main.repos.TaskRepository;
import com.example.main.repos.TeacherRepository;
import com.example.main.service.TeacherService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Primary
public class TeacherServiceImpl implements TeacherService {
    
    //Необходимые поля
    private final TeacherRepository reposTeacher;

    private final StudentRepository reposStudent;

    private final TaskRepository reposTask;

    private final PlaygroundRepository reposPlay;

    private final CellRepository reposCell;

    private final SolutionRepository reposSolut;

    //Работа с Учителем

    @Override
    public void changePassword(Long idTeacher, String newpassword) 
    {
        Teacher teacher = reposTeacher.findById(idTeacher).orElseThrow();
        teacher.setTeacherPassword(newpassword);
    }


    //Работа со студентом

    @Override
    public Long searchStudentInitial(String Initial) 
    {
        return reposStudent.getIdStudentByInitials(Initial);
    }

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
        student.addTask(reposTask.findById(idTask).get());
    }
    
    @Override
    public void deleteTestStudent(Long idStudent, Long idTask) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();
        student.deleteTask(reposTask.findById(idTask).get());
    }
    
    @Override
    public Task getTask(Long idTask) 
    {
        return reposTask.findById(idTask).get();
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

    @Override
    public void deleteTask(Long idTask) 
    {
        reposTask.deleteById(idTask);
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


    //Журнал
    @Override
    public Integer getMarkByStydentByTask(Long idStudent, Long idTask) 
    {
        return reposTeacher.findMarkByStudentAndTask(idStudent, idTask);
    }

    @Override
    public List<Integer> getMarksByStydent(Long idStudent) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow();        
        List<Integer> marksStydent = new ArrayList<>();
        for (Task tasks : student.getTasks()) 
        {
            marksStydent.add(reposTeacher.findMarkByStudentAndTask(idStudent, tasks.getTaskId()));
        }
        return marksStydent;
    }

    @Override
    public void setMarkbyStydent(Long idStudent, Long idTask, Integer mark) 
    {
        Long idSolution = reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask);
        Solution soluti = reposSolut.findById(idSolution).orElseThrow();
        soluti.setMark(mark);  
    }

    //Работа с решением

    @Override
    public Sequence getSequenceStydent(Long idStudent, Integer Task) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSequenceStydent'");
    }

    
    //TODO смена фио учителю
    
    
    
    
    
    
    public void auth(String log, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'auth'");
    }


    @Override
    public Boolean checkSequence(Sequence checkSequence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkSequence'");
    }

    @Override
    public void getAnswerBySequence(String Answer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAnswerBySequence'");
    }


    








   
   
    

    



    










}
