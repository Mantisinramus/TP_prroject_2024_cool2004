package com.example.main.service.impl;

import java.util.List;

import com.example.main.DataModel.Action;
import com.example.main.DataModel.GridSize;
import com.example.main.DataModel.Position;
import com.example.main.DataModel.TaskData;
import com.example.main.service.SolutionService;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.model.Solution;
import com.example.main.model.Task;

import com.example.main.repos.SolutionRepository;
import com.example.main.repos.TaskRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Primary
public class SolutionServiceImpl implements SolutionService
{

    private final SolutionRepository reposSolut;

    private final TaskRepository reposTask;


    @Override
    public Boolean checkSequence(Long idStudent, Long idTask) 
    {
        try {

            Solution solution = reposSolut.findById(reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask)).orElseThrow();
            Task task = reposTask.findById(idTask).orElseThrow();


            // Инициализация ObjectMapper для парсинга JSON
            ObjectMapper objectMapper = new ObjectMapper();

            //парсим JSON задания
            TaskData taskData = objectMapper.readValue(task.getField(), TaskData.class);
            List<Action> actions = objectMapper.readValue(solution.getSequenceText(), objectMapper.getTypeFactory().constructCollectionType(List.class, Action.class));
            
            // Начальные данные из задания
            Position player = taskData.getPlayer();
            List<Position> potions = taskData.getPotions();
            Integer potionsCount = taskData.getPotions().size();
            Position cauldron = taskData.getCauldron();
            List<Position> walls = taskData.getWalls();
            GridSize gridSize = taskData.getGridSize();

            //выполнение шагов
            for (Action action : actions) 
            {
                if (action.getAction().equals("repeat"))
                {
                    // Выполнение повторяющихся действий
                    for (int i = 0; i < action.getTimes(); i++)
                    {
                        if (!performActions(action.getActions(), player, potions, cauldron, walls, gridSize, potionsCount)) {
                            return false;  // Если что-то пошло не так в процессе повторений
                        }
                    }
                    
                } else {
                    //одиночка
                    if (!performAction(action, player, potions, cauldron, walls, gridSize, potionsCount)) {
                        return false;  // Если что-то пошло не так в процессе повторений
                    }
                }
            }
            if(potionsCount == 0) return true;
        } catch (Exception e) {
        }
                return false;
    }
    // Метод для выполнения одиночного действия
    private boolean performAction(Action action, Position player, List<Position> potions, Position cauldron, List<Position> walls, GridSize gridSize, Integer countPos) {
        switch (action.getAction()) {
            case "move_up":
                player.setY(player.getY() - action.getSteps());
                for (Position potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setY(potion.getY() - action.getSteps());
                    }
                }
                break;
            case "move_down":
                player.setY(player.getY() + action.getSteps());
                for (Position potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setY(potion.getY() + action.getSteps());
                    }
                }
                break;
            case "move_left":
                player.setX(player.getX() - action.getSteps());
                for (Position potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setX(potion.getX() - action.getSteps());
                    }
                }
                break;
            case "move_right":
                player.setX(player.getX() + action.getSteps());
                for (Position potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setX(potion.getX() + action.getSteps());
                    }
                }
                break;
            default:
                return false;  // Неизвестное действие
        }

        // Проверка на столкновение со стеной
        if (walls.contains(player)) {
            return false;
        }

        // Проверка на выход за пределы поля
        if (player.getX() < 0 || player.getX() >= gridSize.getWidth() || player.getY() < 0 || player.getY() >= gridSize.getHeight()) {
            return false;
        }

        // Проверка на уничтожение зелий при попадании в котёл
        for (Position potion : potions) {
            if (potion.equals(cauldron)) {
                potions.remove(potion);
                countPos--;  // уменьшаем зелья))))))) пися попа
                break;
            }
        }

        return true;
    }

    // Метод для выполнения набора действий
    private boolean performActions(List<Action> actions, Position player, List<Position> potions, Position cauldron, List<Position> walls, GridSize gridSize, Integer countPos) {
        for (Action action : actions) {
            if (!performAction(action, player, potions, cauldron, walls, gridSize, countPos)) {
                return false;  // Если одно из действий не удалось выполнить
            }
        }
        return true;
    }

}
