package com.example.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.model.Solution;
import com.example.main.model.Student;
import com.example.main.model.Task;
import com.example.main.model.Teacher;
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
            return (long)0;
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
    
   
    

    //Работа с тестом

    @Override
    public void addTestStudent(Long idStudent, Long idTask) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow(() -> new RuntimeException("Student not found"));
        Task task = reposTask.findById(idTask).orElseThrow(() -> new RuntimeException("Student not found"));
      //  student.getTasksId().add(idTask);
        Solution sol = new Solution();
        sol.setStudent(student);
        sol.setTask(task);
        sol.setMark(0); // Устанавливаем начальную оценку
        sol.setSequenceText(""); // Пустой текст решения
        reposStudent.save(student);
        reposSolut.save(sol);
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
        reposSolut.save(soluti);
    }

    //Работа с решением
    @Override
    public void editTask(Long idTask, Task updatedTask) 
    {
        Task existingTask = reposTask.findById(idTask).orElseThrow();

        // Обновляем поля задачи с новыми значениями
        existingTask.setTaskName(updatedTask.getTaskName());
        existingTask.setTaskText(updatedTask.getTaskText());
        existingTask.setPotions(updatedTask.getPotions());
        existingTask.setWalls(updatedTask.getWalls());
        existingTask.setPlayer(updatedTask.getPlayer());

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

    @Override
    public Optional<Teacher> getTeacher(Long idTeacher) {
      return  reposTeacher.findById(idTeacher);
    }

    @Override
    public Long changeLogin(Long idTeacher, String password, String newLogin) {
        
        Teacher teacher = reposTeacher.findById(idTeacher).orElseThrow();
        if(teacher.getTeacherPassword().equals(password))
        {
            teacher.setTeacherLogin(newLogin);
            reposTeacher.save(teacher);
            return teacher.getTeacherId();
        }
        else
        {
            return (long) 0;
        }
    }



    @Override
    public Long changePassword(Long idTeacher, String oldPassword, String newPassword) 
    {
        Teacher teacher = reposTeacher.findById(idTeacher).orElseThrow();
        if(teacher.getTeacherPassword().equals(oldPassword))
        {
            teacher.setTeacherPassword(newPassword);
            reposTeacher.save(teacher);
            return teacher.getTeacherId();
        }
        else
        {
            return (long) 0;
        }
    }


    @Override
    public void changePassStudent(Long idStudent, String password) 
    {
        Student student = reposStudent.findById(idStudent).get();
        student.setStudentPassword(password);
        reposStudent.save(student);
    }

    @Override
    public void changeInitialsStudent(Long idStudent, String initials) {
        Student student = reposStudent.findById(idStudent).get();
        student.setStudentInitials(initials);
        reposStudent.save(student);
    }

    @Override
    public void changeLoginStudent(Long idStudent, String login) {
        Student student = reposStudent.findById(idStudent).get();
        student.setStudentLogin(login);
        reposStudent.save(student);   
    }
}    