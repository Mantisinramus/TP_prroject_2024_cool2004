package com.example.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.main.DataModel.PositionDataModel;
import com.example.main.model.Task;
import com.example.main.model.Teacher;
import com.example.main.repos.TaskRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // Обработка PATCH запроса для изменения только определенных полей
    @PatchMapping("/modern/{id}")
    @Transactional
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        // Находим задание по ID
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        // Обновляем только переданные поля
        fields.forEach((key, value) -> {
            switch (key) {
                case "taskName":
                    task.setTaskName((String) value);
                    break;
                case "taskText":
                    task.setTaskText((String) value);
                    break;
                case "potions":
                    task.setPotions((List<PositionDataModel>) value); // Обновление коллекции potions
                    break;
                case "walls":
                    task.setWalls((List<PositionDataModel>) value); // Обновление коллекции walls
                    break;
                case "player":
                    task.setPlayer((PositionDataModel) value); // Обновление поля player
                    break;
                case "cauldron":
                    task.setCauldron((PositionDataModel) value); // Обновление поля cauldron
                    break;
                case "gridSize":
                    task.setGridSize((PositionDataModel) value); // Обновление поля gridSize
                    break;
                case "teacher":
                    task.setTeacher((Teacher) value); // Обновление поля teacher
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid field: " + key);
            }
        });

        // Сохраняем обновленное задание
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }
}
