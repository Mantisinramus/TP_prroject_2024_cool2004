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
    @SuppressWarnings("unchecked")
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
                    String taskName = (String) value;
                    if (taskName == null || taskName.length() < 9 || taskName.length() > 20 || taskRepository.findByTaskName(taskName).isPresent()) {
                        throw new IllegalArgumentException("Длина наименования задачи должна быть от 9 до 20 символов, и название должно быть уникальным!");
                    }
                    task.setTaskName(taskName);
                    break;
                case "taskText":
                    String taskText = (String) value;
                    if (taskText == null || taskText.length() < 5 || taskText.length() > 300) {
                        throw new IllegalArgumentException("Длина задания должна быть от 5 до 300 символов!");
                    }
                    task.setTaskText(taskText);
                    break;
                case "potions":
                    List<PositionDataModel> potions = objectMapper.convertValue(value, new TypeReference<List<PositionDataModel>>() {});
                    int potionCount = potions == null ? 0 : potions.size();
                    if (potionCount < 1 || potionCount > 2) {
                        throw new IllegalArgumentException("На поле должно быть от 1 до 2 зелий!");
                    }
                    task.setPotions(potions);
                    break;
                case "walls":
                    List<PositionDataModel> walls = objectMapper.convertValue(value, new TypeReference<List<PositionDataModel>>() {});
                    task.setWalls(walls);
                    break;
                case "player":
                    PositionDataModel player = objectMapper.convertValue(value, PositionDataModel.class);
                    if (player == null) {
                        throw new IllegalArgumentException("Игрок должен быть задан!");
                    }
                    task.setPlayer(player);
                    break;
                case "cauldron":
                    PositionDataModel cauldron = objectMapper.convertValue(value, PositionDataModel.class);
                    if (cauldron == null) {
                        throw new IllegalArgumentException("Котел должен быть задан!");
                    }
                    task.setCauldron(cauldron);
                    break;
                case "gridSize":
                    PositionDataModel gridSize = objectMapper.convertValue(value, PositionDataModel.class);
                    int gridWidth = gridSize.getX();
                    int gridHeight = gridSize.getY();
                    if (gridWidth < 5 || gridWidth > 15 || gridHeight < 5 || gridHeight > 15) {
                        throw new IllegalArgumentException("Размер игрового поля должен быть от 5 до 15 по каждой стороне!");
                    }
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
        
        // Проверки после установки всех значений
        int gridWidth = task.getGridSize().getX();
        int gridHeight = task.getGridSize().getY();
        int totalCells = gridWidth * gridHeight;
        
        int occupiedCells = 0;
        if (task.getWalls() != null) {
            occupiedCells += task.getWalls().size();
        }
        if (task.getPotions() != null) {
            occupiedCells += task.getPotions().size();
        }
        if (task.getPlayer() != null) {
            occupiedCells += 1;
        }
        if (task.getCauldron() != null) {
            occupiedCells += 1;
        }
        
        double occupiedPercentage = (double) occupiedCells / totalCells * 100;
        if (occupiedPercentage > 40) {
            throw new IllegalArgumentException("Занятые клетки (стены, зелья, котел и игрок) не могут занимать более 40% от общего количества клеток!");
        }

        // Сохраняем обновленное задание
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }
}
