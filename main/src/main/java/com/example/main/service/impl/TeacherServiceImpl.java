package com.example.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.Sequence;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.model.Student;
import com.example.main.model.Task;
import com.example.main.model.Teacher;
import com.example.main.model.Solution;
import com.example.main.repos.SolutionRepository;
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
    private final TeacherRepository reposTeacher;

    private final StudentRepository reposStudent;

    private final TaskRepository reposTask;

    private final SolutionRepository reposSolut;

    @Override
    public void addTeacher(Teacher teacher)
    {
        reposTeacher.save(teacher);
    }

    //Работа с Учителем
    @Override
    public Long auth(String log, String password) 
    {
        // Ищем ID учитель по логину
        Long teacherId = reposTeacher.findTeacherIdByLogin(log);
    
        // Если учитель с таким логином не найден
        if (teacherId == null) {
            // throw new StudentNotFoundException("Student not found with login: " + log);
        }   
    
        // Если учитель найден, извлекаем его из базы
        Teacher teacher = reposTeacher.findById(teacherId).get();
    
        // Проверяем пароль
        if (teacher.getTeacherPassword().equals(password)) {
            // Если пароль верный, возвращаем ID учителя
            return teacherId;
        } else {
            // Если пароль неверный, выбрасываем исключение
            //throw new IncorrectPasswordException("Incorrect password for student with login: " + log);
            return (long)0;
        }
    }

    @Override
    public void changePassword(Long idTeacher, String newpassword) 
    {
        Teacher teacher = reposTeacher.findById(idTeacher).orElseThrow();
        teacher.setTeacherPassword(newpassword);
        reposTeacher.save(teacher);
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
        Student student = reposStudent.findById(idStudent).get();
        student.setStudentPassword(password);
    }
    

    //Работа с тестом

    @Override
    public void addTestStudent(Long idStudent, Long idTask) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow(() -> new RuntimeException("Student not found"));
        student.getTasksId().add(idTask);
        reposStudent.save(student);
    }
    
    @Override
    public void deleteTestStudent(Long idStudent, Long idTask) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow(() -> new RuntimeException("Student not found"));
        student.getTasksId().remove(idTask);
        reposStudent.save(student);
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
    public void addTask(Task task) 
    {
        reposTask.save(task);
    }

    @Override
    public void deleteTask(Long idTask) 
    {
        reposTask.deleteById(idTask);
    }




    //Журнал
    @Override
    public Integer getMarkByStydentByTask(Long idStudent, Long idTask) 
    {
        Solution sol = reposSolut.findById(reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask)).orElseThrow(() -> new RuntimeException("Student not found"));
        return sol.getMark();
    }

    @Override
    public List<Integer> getMarksByStydent(Long idStudent) 
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

    @Override
    public void setMarkbyStydent(Long idStudent, Long idTask, Integer mark) 
    {
        Long idSolution = reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask);
        Solution soluti = reposSolut.findById(idSolution).orElseThrow();
        soluti.setMark(mark);  
    }

    //Работа с решением
    @Override
    public void editTask(Long idTask, Task updatedTask) 
    {
        Task existingTask = reposTask.findById(idTask).orElseThrow();

        // Обновляем поля задачи с новыми значениями
        existingTask.setTaskName(updatedTask.getTaskName());
        existingTask.setTaskText(updatedTask.getTaskText());
        existingTask.setField(updatedTask.getField());

        reposTask.save(existingTask);
    }

    @Override
    public Solution getSolutionStydent(Long idStudent, Long idTask) 
    {
        return reposSolut.findById(reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask)).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public void setAnswerBySequence(Long idStudent, Long idTask, String Answer) 
    {
        Solution editSolution = reposSolut.findById(reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask)).orElseThrow(() -> new RuntimeException("Student not found"));
        editSolution.setTeacherAnswer(Answer);
        reposSolut.save(editSolution);
    }

}    