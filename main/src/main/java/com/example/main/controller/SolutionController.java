package com.example.main.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.DataModel.GameStateDTO;
import com.example.main.model.Task;
import com.example.main.service.SolutionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/solution")
@AllArgsConstructor
public class SolutionController 
{
    private final SolutionService solutionService;

    @GetMapping("/checkSequence/{idStudent}/{idTask}")
    public ResponseEntity<List<GameStateDTO>> checkSequence(@PathVariable Long idStudent, @PathVariable Long idTask) 
    {
      List<GameStateDTO> gameStates = solutionService.checkSequence(idStudent, idTask);
      return ResponseEntity.ok(gameStates);
    }


    @GetMapping("/taskName/{idSolution}")
    public ResponseEntity<List<Task>> findTaskNameBySolutionId(@PathVariable Long idSolution) 
    {
       return ResponseEntity.ok(solutionService.findTaskBySolutionId(idSolution));
    }

    @GetMapping("/generate-random")
    public ResponseEntity<?> generateRandomTask(
        @RequestParam int rows,
        @RequestParam int cols,
        @RequestParam int numPotions,
        @RequestParam Long idTeacher) {
    try {
        // Валидация входных параметров
        if (rows < 5 || rows > 15 || cols < 5 || cols > 15) {
            return ResponseEntity.badRequest().body("Размер игрового поля должен быть от 5 до 15 по каждой стороне!");
        }
        if (numPotions < 1 || numPotions > 2) {
            return ResponseEntity.badRequest().body("Количество зелий должно быть от 1 до 2!");
        }

        // Генерация случайной задачи
        Task generatedTask = solutionService.randomGenerateTask(rows, cols, numPotions, idTeacher);

        return ResponseEntity.ok(generatedTask);

    } catch (IllegalArgumentException e) {
        // Логика обработки ошибок, вызванных некорректными данными
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        // Общая обработка ошибок
        e.printStackTrace(); // Вывод трассировки ошибки в логи
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
    }
}
}
