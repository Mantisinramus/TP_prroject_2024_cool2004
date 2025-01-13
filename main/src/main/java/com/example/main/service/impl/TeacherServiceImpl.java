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

import jakarta.transaction.Transactional;
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
        // Проверка длины ФИО
        if (teacher.getTeacherInitials() == null || teacher.getTeacherInitials().length() < 10 || teacher.getTeacherInitials().length() > 80) 
        {
        throw new IllegalArgumentException("Длина ФИО должна быть от 10 до 80 символов!");
        }
        // Проверка длины пароля
        if (teacher.getTeacherLogin() == null || teacher.getTeacherLogin().length() < 4 || teacher.getTeacherLogin().length() > 8) 
        {
            throw new IllegalArgumentException("Логин от 4 до 8 символов!");
        }    
        // Проверка длины пароля
        if (teacher.getTeacherPassword() == null || teacher.getTeacherPassword().length() < 4 || teacher.getTeacherPassword().length() > 12) {

            throw new IllegalArgumentException("Пароль от 4 до 12 символов!");
        }
        


        reposTeacher.save(teacher);
    }

    //Работа с Учителем
    @Override
    public Long auth(String log, String password) 
    {

        // Проверка длины пароля
        if (log == null || log.length() < 4 || log.length() > 8) 
        {
        // Возвращаем 0, если пароль не соответствует требованиям
        return (long) 0;
        }    
        // Проверка длины пароля
        if (password == null || password.length() < 4 || password.length() > 12) {
        // Возвращаем 0, если пароль не соответствует требованиям
        return (long) 0;
        }

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

    @Transactional
    public void addStudent(Student student)
    {
        List<Student> allStudents = findAllStudents();
    
        // Проверяем, если количество студентов больше 100
        if (allStudents.size() > 100) {
            throw new IllegalArgumentException("Достигнут лимит студентов (максимум 100)");
        }

        if (student.getStudentInitials() == null || student.getStudentInitials().length() < 10 || student.getStudentInitials().length() > 80) {
            throw new IllegalArgumentException("Длина ФИО должна быть от 10 до 80 символов!");
        }

        // Проверка длины пароля
        if (student.getStudentLogin() == null || student.getStudentLogin().length() < 4 || student.getStudentLogin().length() > 8) 
        {
            throw new IllegalArgumentException("Логин от 4 до 8 символов!");
        }    
        // Проверка длины пароля
        if (student.getStudentPassword() == null || student.getStudentPassword().length() < 4 || student.getStudentPassword().length() > 12) {

            throw new IllegalArgumentException("Пароль от 4 до 12 символов!");
        }

        if (reposStudent.findByStudentLogin(student.getStudentLogin()).isPresent()) {
            throw new IllegalArgumentException("Логин уже занят!");
        }
        reposStudent.save(student);

    }

    @Transactional
    public void deleteStudentById(Long idStudent) 
    {
        // Сначала удаляем все связанные решения
        reposSolut.deleteByStudentId(idStudent);
    
        // Затем удаляем самого ученика
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
    
    @Transactional
    public void deleteTestStudent(Long idStudent, Long idTask) 
    {
        Student student = reposStudent.findById(idStudent).orElseThrow(() -> new RuntimeException("Student not found"));
        student.getTasksId().remove(idTask);
        reposStudent.save(student);
        reposSolut.deleteByStudentIdTaskId(idStudent, idTask);
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

    List<Task> allTask = getAllTasks();

    // Получаем размеры сетки
    int gridWidth = task.getGridSize().getX();
    int gridHeight = task.getGridSize().getY();

    // Рассчитываем общее количество клеток
    int totalCells = gridWidth * gridHeight;

    // Рассчитываем количество занятых клеток (стены, зелья, котел, игрок)
    int occupiedCells = 0;

    // Стены
    if (task.getWalls() != null) {
        occupiedCells += task.getWalls().size();
    }

    // Зелья
    if (task.getPotions() != null) {
        occupiedCells += task.getPotions().size();
    }

    // Игрок
    if (task.getPlayer() != null) {
        occupiedCells += 1; // Игрок занимает одну клетку
    }

    // Котел
    if (task.getCauldron() != null) {
        occupiedCells += 1; // Котел занимает одну клетку
    }

    // Проверка, что занятые клетки не превышают 40% от общего числа клеток
    double occupiedPercentage = (double) occupiedCells / totalCells * 100;
    if (occupiedPercentage > 40) {
        throw new IllegalArgumentException("Занятые клетки (стены, зелья, котел и игрок) не могут занимать более 40% от общего количества клеток!");
    }

    // Проверяем, если количество задач больше 50
    if (allTask.size() > 50) {
        throw new IllegalArgumentException("Достигнут лимит задач (максимум 50)");
    }

    // Проверка длины названия задачи
    if (task.getTaskName() == null || task.getTaskName().length() < 9 || task.getTaskName().length() > 20 || reposTask.findByTaskName(task.getTaskName()).isPresent())  
    {
        throw new IllegalArgumentException("Длина наименования задачи от 9 до 20");
    }    
    // Проверка размера длины текста задания
    if (task.getTaskText() == null || task.getTaskText().length() < 5 || task.getTaskText().length() > 300) {

        throw new IllegalArgumentException("Длина задания от 5 до 300");
    }
    // Проверка размеров игрового поля
    if (task.getGridSize().getX() < 5 || task.getGridSize().getX() > 15  || task.getGridSize().getY() < 5 || task.getGridSize().getY() > 15)
    {
        throw new IllegalArgumentException("Размер игрового поля должен быть от 5 до 15 по каждой стороне!");
    }
    // Проверка наличия игрока и котла
    if (task.getPlayer() == null)
    {
        throw new IllegalArgumentException("Игрок должен быть задан!");
    }
    if (task.getCauldron() == null) 
    {
        throw new IllegalArgumentException("Котел должен быть задан!");
    }
    // Проверка количества зелий
    int potionCount = task.getPotions() == null ? 0 : task.getPotions().size();
    if (potionCount < 1 || potionCount > 2) 
    {
        throw new IllegalArgumentException("На поле должно быть от 1 до 2 зелий!");
    }
        reposTask.save(task);
    }

    @Transactional
    public void deleteTask(Long idTask) 
    {
        reposTask.deleteById(idTask);
        reposSolut.deleteByTaskId(idTask);
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
        // Проверка на допустимый диапазон для оценки
        if (mark < 2 || mark > 5) {
        throw new IllegalArgumentException("Оценка должна быть от 2 до 5.");
        }
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


    // Получаем размеры сетки
    int gridWidth = updatedTask.getGridSize().getX();
    int gridHeight = updatedTask.getGridSize().getY();

    // Рассчитываем общее количество клеток
    int totalCells = gridWidth * gridHeight;

    // Рассчитываем количество занятых клеток (стены, зелья, котел, игрок)
    int occupiedCells = 0;

    // Стены
    if (updatedTask.getWalls() != null) {
        occupiedCells += updatedTask.getWalls().size();
    }

    // Зелья
    if (updatedTask.getPotions() != null) {
        occupiedCells += updatedTask.getPotions().size();
    }

    // Игрок
    if (updatedTask.getPlayer() != null) {
        occupiedCells += 1; // Игрок занимает одну клетку
    }

    // Котел
    if (updatedTask.getCauldron() != null) {
        occupiedCells += 1; // Котел занимает одну клетку
    }

    // Проверка, что занятые клетки не превышают 40% от общего числа клеток
    double occupiedPercentage = (double) occupiedCells / totalCells * 100;
    if (occupiedPercentage > 40) {
        throw new IllegalArgumentException("Занятые клетки (стены, зелья, котел и игрок) не могут занимать более 40% от общего количества клеток!");
    }
    // Проверка длины названия задачи
    if (updatedTask.getTaskName() == null || updatedTask.getTaskName().length() < 9 || updatedTask.getTaskName().length() > 20|| reposTask.findByTaskName(updatedTask.getTaskName()).isPresent()) 
    {
        throw new IllegalArgumentException("Длина наименования задачи от 9 до 20");
    }    
    // Проверка размера длины текста задания
    if (updatedTask.getTaskText() == null || updatedTask.getTaskText().length() < 5 || updatedTask.getTaskText().length() > 300) {

        throw new IllegalArgumentException("Длина задания от 5 до 300");
    }
    // Проверка размеров игрового поля
    if (updatedTask.getGridSize().getX() < 5 || updatedTask.getGridSize().getX() > 15 || updatedTask.getGridSize().getY() < 5 || updatedTask.getGridSize().getY() > 15) 
    {
    throw new IllegalArgumentException("Размер игрового поля должен быть от 5 до 15 по каждой стороне!");
    }

    // Проверка наличия игрока и котла
    if (updatedTask.getPlayer() == null) 
    {
        throw new IllegalArgumentException("Игрок должен быть задан!");
    }
    if (updatedTask.getCauldron() == null) 
    {
        throw new IllegalArgumentException("Котел должен быть задан!");
    }

    // Проверка количества зелий
    int potionCount = updatedTask.getPotions() == null ? 0 : updatedTask.getPotions().size();
    if (potionCount < 1 || potionCount > 2) 
    {
        throw new IllegalArgumentException("На поле должно быть от 1 до 2 зелий!");
    }

        // Обновляем поля задачи с новыми значениями
        existingTask.setTaskName(updatedTask.getTaskName());
        existingTask.setTaskText(updatedTask.getTaskText());
        existingTask.setPotions(updatedTask.getPotions());
        existingTask.setWalls(updatedTask.getWalls());
        existingTask.setPlayer(updatedTask.getPlayer());
        existingTask.setCauldron(updatedTask.getCauldron());
        existingTask.setGridSize(updatedTask.getGridSize());

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

        // Проверка длины пароля
        if (newLogin == null || newLogin.length() < 4 || newLogin.length() > 8) 
        {
        // Возвращаем 0, если пароль не соответствует требованиям
        return (long) 0;
        }    
        // Проверка длины пароля
        if (password == null || password.length() < 4 || password.length() > 12) {
        // Возвращаем 0, если пароль не соответствует требованиям
        return (long) 0;
        }
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

        // Проверка длины пароля
        if (newPassword == null || newPassword.length() < 4 || newPassword.length() > 12) 
        {
        // Возвращаем 0, если пароль не соответствует требованиям
        return (long) 0;
        }    

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
            // Проверка длины пароля
        if (password == null || password.length() < 4 || password.length() > 12) {

        throw new IllegalArgumentException("Пароль от 4 до 12 символов!");
        }
        student.setStudentPassword(password);
        reposStudent.save(student);
    }

    @Override
    public void changeInitialsStudent(Long idStudent, String initials) {
        Student student = reposStudent.findById(idStudent).get();
        if (initials == null || initials.length() < 10 || initials.length() > 80) {
            throw new IllegalArgumentException("Длина ФИО должна быть от 10 до 80 символов!");
        }
        student.setStudentInitials(initials);
        reposStudent.save(student);
    }

    @Override
    public void changeLoginStudent(Long idStudent, String login) {
        Student student = reposStudent.findById(idStudent).get();
        if (reposStudent.findByStudentLogin(login).isPresent()) {
            throw new IllegalArgumentException("Логин уже занят!");
        }
        // Проверка логина пароля
        if (login == null || login.length() < 4 || login.length() > 8) {
            throw new IllegalArgumentException("Логин от 4 до 8 символов!");
        }    
        student.setStudentLogin(login);
        reposStudent.save(student);   
    }

    @Override
    public Long getStudentIdByLogin(String login) {
       return reposStudent.findStudentIdByLogin(login);
    }



    





}    