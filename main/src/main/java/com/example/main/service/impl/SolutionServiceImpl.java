package com.example.main.service.impl;

import java.util.ArrayList;
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
        // try {

        //     Solution solution = reposSolut.findById(reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask)).orElseThrow();
        //     Task task = reposTask.findById(idTask).orElseThrow();

        //     String sequenceText = solution.getSequenceText();
        //     String[] commands = sequenceText.split(",");
            
        //     // Начальные данные из задания
        //     PositionDataModel player = task.getPlayer();
        //     List<PositionDataModel> potions = task.getPotions();
        //     Integer potionsCount = task.getPotions().size();
        //     PositionDataModel cauldron = task.getCauldron();
        //     List<PositionDataModel> walls = task.getWalls();
        //     PositionDataModel gridSize = task.getGridSize();

        //     //выполнение шагов
        //     for (Action command : commands) 
        //     {   
        //         if (command.startsWith(""))
        //         {

        //             // Выполнение повторяющихся действий
        //             for (int i = 0; i < action.getTimes(); i++)
        //             {
        //                 if (!performActions(action.getActions(), player, potions, cauldron, walls, gridSize, potionsCount)) {
        //                     return false;  // Если что-то пошло не так в процессе повторений
        //                 }
        //             }
                    
        //         } else {
        //             //одиночка
        //             if (!performAction(action, player, potions, cauldron, walls, gridSize, potionsCount)) {
        //                 return false;  // Если что-то пошло не так в процессе повторений
        //             }
        //         }
        //     }
        //     if(potionsCount == 0) return true;
        // } catch (Exception e) {
        // }
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

    private List<Action> parseSequence(String sequenceText) {
    // List<Action> actions = new ArrayList<>();
    // String[] commands = sequenceText.split(",");

    // for (String command : commands) {
    //     command = command.trim();
    //     if (command.startsWith("повторить")) {
    //         Pattern repeatPattern = Pattern.compile("повторить (\\d+)\\((.+)\\)");
    //         Matcher matcher = repeatPattern.matcher(command);
    //         if (matcher.matches()) {
    //             int repeatCount = Integer.parseInt(matcher.group(1));
    //             String innerCommands = matcher.group(2);

    //             // Рекурсивный вызов для вложенных команд
    //             List<Action> subActions = parseSequence(innerCommands);
    //             actions.add(new Action("repeat", repeatCount, subActions));
    //         }
    //     } else {
    //         // Простая команда
    //         String[] parts = command.split(" ");
    //         if (parts.length == 2) {
    //             String actionName = parts[0];
    //             int steps = Integer.parseInt(parts[1]);
    //             actions.add(new Action(actionName, steps));
    //         } else {
    //             throw new IllegalArgumentException("Некорректная команда: " + command);
    //         }
    //     }
    // }
    return null;
   // return actions;
}
    @Override
    public List<Task> findTaskBySolutionId(Long idSolution) {
        return reposTask.findTaskBySolutionId(idSolution);
    }

}
