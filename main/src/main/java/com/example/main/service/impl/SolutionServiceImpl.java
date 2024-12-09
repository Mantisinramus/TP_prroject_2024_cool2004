package com.example.main.service.impl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.DataModel.Action;
import com.example.main.DataModel.PositionDataModel;
import com.example.main.model.Solution;
import com.example.main.model.Task;
import com.example.main.repos.SolutionRepository;
import com.example.main.repos.TaskRepository;
import com.example.main.service.SolutionService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

            List<Action> actions = objectMapper.readValue(solution.getSequenceText(), objectMapper.getTypeFactory().constructCollectionType(List.class, Action.class));
            
            // Начальные данные из задания
            PositionDataModel player = task.getPlayer();
            List<PositionDataModel> potions = task.getPotions();
            Integer potionsCount = task.getPotions().size();
            PositionDataModel cauldron = task.getCauldron();
            List<PositionDataModel> walls = task.getWalls();
            PositionDataModel gridSize = task.getGridSize();

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
    private boolean performAction(Action action, PositionDataModel player, List<PositionDataModel> potions, PositionDataModel cauldron, List<PositionDataModel> walls, PositionDataModel gridSize, Integer countPos) {
        switch (action.getAction()) {
            case "move_up":
                player.setY(player.getY() - action.getSteps());
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setY(potion.getY() - action.getSteps());
                    }
                }
                break;
            case "move_down":
                player.setY(player.getY() + action.getSteps());
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setY(potion.getY() + action.getSteps());
                    }
                }
                break;
            case "move_left":
                player.setX(player.getX() - action.getSteps());
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setX(potion.getX() - action.getSteps());
                    }
                }
                break;
            case "move_right":
                player.setX(player.getX() + action.getSteps());
                for (PositionDataModel potion : potions) {
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
        if (player.getX() < 0 || player.getX() >= gridSize.getX() || player.getY() < 0 || player.getY() >= gridSize.getY()) {
            return false;
        }

        // Проверка на уничтожение зелий при попадании в котёл
        for (PositionDataModel potion : potions) {
            if (potion.equals(cauldron)) {
                potions.remove(potion);
                countPos--;  // уменьшаем зелья))))))) пися попа
                break;
            }
        }

        return true;
    }

    // Метод для выполнения набора действий
    private boolean performActions(List<Action> actions, PositionDataModel player, List<PositionDataModel> potions, PositionDataModel cauldron, List<PositionDataModel> walls, PositionDataModel gridSize, Integer countPos) {
        for (Action action : actions) {
            if (!performAction(action, player, potions, cauldron, walls, gridSize, countPos)) {
                return false;  // Если одно из действий не удалось выполнить
            }
        }
        return true;
    }

}
