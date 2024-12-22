package com.example.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.model.Solution;
import com.example.main.model.Task;
import com.example.main.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController 
{
    private final StudentService studentService;

    // Авторизация студента
    @PostMapping("/auth")
    public ResponseEntity<Long> auth(@RequestParam String log, @RequestParam String password) {
        Long studentId = studentService.auth(log, password);
        
        if (studentId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(studentId); // Неверные данные
        }
        
        return ResponseEntity.ok(studentId);  // Возвращаем ID студента
    }

    // Получение задачи по ID
    @GetMapping("/{idStudent}/tasks/{idTask}")
    public ResponseEntity<Task> getTask(@PathVariable Long idTask) {
        Task task = studentService.getTask(idTask);
        
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Задача не найдена
        }
        
        return ResponseEntity.ok(task);  // Возвращаем задачу
    }

    // Получение всех задач студента
    @GetMapping("/{idStudent}/tasks")
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable Long idStudent) {
        List<Task> tasks = studentService.getAllTasks(idStudent);
        return ResponseEntity.ok(tasks);  // Возвращаем все задачи студента
    }

   

    // Отправка ответа на задачу
    @PutMapping("/{idStudent}/tasks/{idTask}/answer")
    public ResponseEntity<String> postAnswerTask(
            @PathVariable Long idStudent,
            @PathVariable Long idTask,
            @RequestParam String answerTask) {
        
        studentService.postAnswerTask(idStudent, idTask, answerTask);
        return ResponseEntity.ok("Answer submitted successfully");
    }

    //TODO получение обычной solution


    // Получение оценки за конкретную задачу
    @GetMapping("/{idStudent}/tasks/{idTask}/mark")
    public ResponseEntity<Integer> getMark(
            @PathVariable Long idStudent,
            @PathVariable Long idTask) {
        
        Integer mark = studentService.getMark(idStudent, idTask);
        
        if (mark == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Оценка не найдена
        }
        
        return ResponseEntity.ok(mark);  // Возвращаем оценку
    }

    // Получение всех оценок студента
    @GetMapping("/{idStudent}/marks")
    public ResponseEntity<List<Integer>> getAllMarks(@PathVariable Long idStudent) {
        List<Integer> marks = studentService.getAllMarks(idStudent);
        return ResponseEntity.ok(marks);  // Возвращаем все оценки студента
    }



    @GetMapping("/{idStudent}/solutions")
    public ResponseEntity<List<Solution>> getAllSolutions(@PathVariable Long idStudent) {
        List<Solution> solutions = studentService.getAllSolutions(idStudent);
        return ResponseEntity.ok(solutions);  // Возвращаем все задачи студента
    }

     // Получение задачи
     @GetMapping("/solutions/{idSolution}")
     public ResponseEntity<Optional<Solution>> getSolution(@PathVariable Long idSolution) {
         return ResponseEntity.ok(studentService.getSolution(idSolution));
     }

}
