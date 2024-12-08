package com.example.main.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.model.Playground;
import com.example.main.model.Student;
import com.example.main.model.Task;
import com.example.main.service.TeacherService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/teacher")
@AllArgsConstructor
public class TeacherController {

        private final TeacherService teacherService;

    // Авторизация учителя
    @PostMapping("/auth")
    public ResponseEntity<Long> auth(@RequestParam String log, @RequestParam String password) {
        Long teacherId = teacherService.auth(log, password);

        if (teacherId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(teacherId); // Неверные данные
        }

        return ResponseEntity.ok(teacherId); // Возвращаем ID учителя
    }

    // Изменение пароля учителя
    @PutMapping("/{idTeacher}/password")
    public ResponseEntity<String> changePassword(
        @PathVariable Long idTeacher,
        @RequestParam String newpassword) {
    teacherService.changePassword(idTeacher, newpassword);
    return ResponseEntity.ok("Password successfully updated");
}

    // Поиск студента по инициалам
    @GetMapping("/students/search")
    public ResponseEntity<Long> searchStudentByInitials(@RequestParam String initials) {
        return ResponseEntity.ok(teacherService.searchStudentInitial(initials));
    }

    // Добавление нового студента
    @PostMapping("/students")
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        teacherService.addStudent(student);
        return ResponseEntity.ok("Student added successfully");
    }

    // Удаление студента
    @DeleteMapping("/students/{idStudent}")
    public ResponseEntity<String> deleteStudentById(@PathVariable Long idStudent) {
        teacherService.deleteStudentById(idStudent);
        return ResponseEntity.ok("Student deleted successfully");
    }

    // Получение информации о студенте
    @GetMapping("/students/{idStudent}")
    public ResponseEntity<Student> getStudent(@PathVariable Long idStudent) {
        return ResponseEntity.ok(teacherService.findStudent(idStudent));
    }

    // Получение всех студентов
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(teacherService.findAllStudents());
    }

    // Изменение пароля студента
    @PutMapping("/students/{idStudent}/password")
    public ResponseEntity<String> changeStudentPassword(
            @PathVariable Long idStudent,
            @RequestParam String password) {
        teacherService.changePassStudent(idStudent, password);
        return ResponseEntity.ok("Student password updated");
    }

    // Добавление задачи студенту
    @PostMapping("/students/{idStudent}/tasks/{idTask}")
    public ResponseEntity<String> assignTaskToStudent(
            @PathVariable Long idStudent,
            @PathVariable Long idTask) {
        teacherService.addTestStudent(idStudent, idTask);
        return ResponseEntity.ok("Task assigned to student");
    }

    // Удаление задачи у студента
    @DeleteMapping("/students/{idStudent}/tasks/{idTask}")
    public ResponseEntity<String> removeTaskFromStudent(
            @PathVariable Long idStudent,
            @PathVariable Long idTask) {
        teacherService.deleteTestStudent(idStudent, idTask);
        return ResponseEntity.ok("Task removed from student");
    }

    // Получение задачи
    @GetMapping("/tasks/{idTask}")
    public ResponseEntity<Task> getTask(@PathVariable Long idTask) {
        return ResponseEntity.ok(teacherService.getTask(idTask));
    }

    // Получение всех задач
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(teacherService.getAllTasks());
    }

    // Добавление задачи
    @PostMapping("/tasks")
    public ResponseEntity<String> addTask(
            @RequestParam Long playgroundId,
            @RequestBody Task task) {
        Playground playground = new Playground();
        playground.setPlaygroundId(playgroundId);
        teacherService.addTask(playground, task);
        return ResponseEntity.ok("Task added successfully");
    }

    // Удаление задачи
    @DeleteMapping("/tasks/{idTask}")
    public ResponseEntity<String> deleteTask(@PathVariable Long idTask) {
        teacherService.deleteTask(idTask);
        return ResponseEntity.ok("Task deleted successfully");
    }

    // Редактирование задачи
    @PutMapping("/tasks/{idTask}")
    public ResponseEntity<String> editTask(
            @PathVariable Long idTask,
            @RequestParam Long playgroundId,
            @RequestBody Task task) {
        Playground playground = new Playground();
        playground.setPlaygroundId(playgroundId);
        teacherService.editTask(playgroundId, idTask, playground, task);
        return ResponseEntity.ok("Task updated successfully");
    }

    // Получение оценки по студенту и задаче
    @GetMapping("/students/{idStudent}/tasks/{idTask}/mark")
    public ResponseEntity<Integer> getMarkByStudentAndTask(
            @PathVariable Long idStudent,
            @PathVariable Long idTask) {
        return ResponseEntity.ok(teacherService.getMarkByStydentByTask(idStudent, idTask));
    }

    // Получение всех оценок студента
    @GetMapping("/students/{idStudent}/marks")
    public ResponseEntity<List<Integer>> getMarksByStudent(@PathVariable Long idStudent) {
        return ResponseEntity.ok(teacherService.getMarksByStydent(idStudent));
    }

    // Установка оценки студенту за задачу
    @PostMapping("/students/{idStudent}/tasks/{idTask}/mark")
    public ResponseEntity<String> setMarkForStudent(
            @PathVariable Long idStudent,
            @PathVariable Long idTask,
            @RequestParam Integer mark) {
        teacherService.setMarkbyStydent(idStudent, idTask, mark);
        return ResponseEntity.ok("Mark updated successfully");
    }
}
