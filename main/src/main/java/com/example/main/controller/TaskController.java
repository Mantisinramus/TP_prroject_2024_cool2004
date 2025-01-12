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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
                    List<PositionDataModel> potions = objectMapper.convertValue(value, new TypeReference<List<PositionDataModel>>() {});
                    task.setPotions(potions);
                    break;
                case "walls":
                    List<PositionDataModel> walls = objectMapper.convertValue(value, new TypeReference<List<PositionDataModel>>() {});
                    task.setWalls(walls);
                    break;
                case "player":
                    PositionDataModel player = objectMapper.convertValue(value, PositionDataModel.class);
                    task.setPlayer(player);
                    break;
                case "cauldron":
                    PositionDataModel cauldron = objectMapper.convertValue(value, PositionDataModel.class);
                    task.setCauldron(cauldron);
                    break;
                case "gridSize":
                    PositionDataModel gridSize = objectMapper.convertValue(value, PositionDataModel.class);
                    task.setGridSize(gridSize);
                    break;
                case "teacher":
                    Teacher teacher = objectMapper.convertValue(value, Teacher.class);
                    task.setTeacher(teacher);
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
