package com.example.main.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.DataModel.GameStateDTO;
import com.example.main.DataModel.PositionDataModel;
import com.example.main.model.Solution;
import com.example.main.model.Task;
import com.example.main.repos.SolutionRepository;
import com.example.main.repos.TaskRepository;
import com.example.main.service.SolutionService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Primary
public class SolutionServiceImpl implements SolutionService
{

    private final SolutionRepository reposSolut;

    private final TaskRepository reposTask;
    
    private PositionDataModel player;
    private List<PositionDataModel> potions;
    private PositionDataModel cauldron;
    private List<PositionDataModel> walls;
    private PositionDataModel gridSize;
    private List<GameStateDTO> gameStates = new ArrayList<>(); // Хранилище состояний игры

    @Override
    public List<GameStateDTO> checkSequence(Long idStudent, Long idTask) 
    {
        try {
            Solution solution = reposSolut.findById(
                    reposSolut.findSolutionIdByStudentIdAndTaskId(idStudent, idTask)
            ).orElseThrow();
            Task task = reposTask.findById(idTask).orElseThrow();
        
            // Получаем команды из sequenceText
            String[] commands = solution.getSequenceText().split(",");
            System.out.println("хуярим это: "+ Arrays.toString(commands));
        
            // Начальные данные из задания
            player = task.getPlayer();
            potions = task.getPotions();
            cauldron = task.getCauldron();
            walls = task.getWalls();
            gridSize = task.getGridSize();
            

            System.out.println("=== Начальные параметры ===");
            System.out.println("Игрок: " + player);
            System.out.println("Зелья: " + potions);
            System.out.println("Котёл: " + cauldron);
            System.out.println("Стены: " + walls);
            System.out.println("Размер поля: " + gridSize);
            System.out.println("============================");
        
            // Выполнение шагов
            for (String command : commands) 
            {
                executeCommand(command);
                System.out.println("Игрок: " + player);
                System.out.println("Зелья: " + potions);
                System.out.println("Осталось зелий: " + potions.size());
            }
            boolean success = potions.size() == 0;
            System.out.println("\nРезультат выполнения: " + (success ? "Успех" : "Неудача"));
            return gameStates;
        } catch (Exception e) {
            e.printStackTrace();
            return gameStates;
        }
    }

    private void executeCommand(String command) {
    command = command.trim();
    
    if (command.startsWith("repeat")) {
        System.out.println("\nзалупа: " + command);
        // Обработка команды "repeat"
        int openIndex = command.indexOf("(");
        int closeIndex = command.lastIndexOf(")");
        
        if (openIndex == -1 || closeIndex == -1 || closeIndex <= openIndex) {
            throw new IllegalArgumentException("Некорректный формат команды: отсутствуют скобки или порядок неверный.");
        }
        
        // Извлечение числа повторений
        String repeatPart = command.substring(0, openIndex).replaceAll("\\D+", "").trim();
        if (repeatPart.isEmpty()) {
            throw new IllegalArgumentException("Некорректный формат команды: число повторений отсутствует.");
        }
        int repeatCount = Integer.parseInt(repeatPart);
        
        // Извлечение команд внутри скобок
        String innerCommands = command.substring(openIndex + 1, closeIndex).trim();
        List<String> parsedCommands = parseCommands(innerCommands);
        
        // Выполняем повторение
        for (int i = 0; i < repeatCount; i++) {
            for (String parsedCommand : parsedCommands) {
                executeCommand(parsedCommand); // Рекурсивно выполняем команды
            }
        }
    } else {
        // Разбор и выполнение отдельных команд
        List<String> commands = parseCommands(command); // Используем парсер для корректного разбора
        for (String singleCommand : commands) {
            performAction(singleCommand.trim());
            // Сохраняем текущее состояние
            PositionDataModel playerCopy = new PositionDataModel();
            playerCopy.setX(player.getX());
            playerCopy.setY(player.getY());
            boolean success = potions.size() == 0;
            gameStates.add(new GameStateDTO(
                playerCopy, // Копия позиции игрока
                new ArrayList<>(potions), // Копия списка зелий
                success
            ));
        }
    }
}
    // Метод для выполнения одиночного действия
    private boolean performAction(String command) {
        System.out.println("Выполнение команды: " + command);
        switch (command) {
            case "up":
                System.out.println("До: " + player);
                player.setY(player.getY() - 1);
                System.out.println("Игрок после выполнения команды: " + player);
                if (walls.contains(player)) {
                    System.out.println("Столкновение со стеной!");
                    player.setY(player.getY() + 1);
                }
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setY(potion.getY() -1);
                        System.out.println("Зелье после выполнения команды: " + potion);
                    }
                }
                break;
            case "down":
                System.out.println("До: " + player);
                player.setY(player.getY() + 1);
                System.out.println("Теперь: " + player);
                if (walls.contains(player)) {
                    System.out.println("Столкновение со стеной!");
                    player.setY(player.getY() - 1);
                }
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setY(potion.getY() + 1);
                        System.out.println("Зелье после выполнения команды: " + potion);
                    }
                }
                break;
            case "left":
                player.setX(player.getX() - 1);
                System.out.println("Игрок после выполнения команды: " + player);

                if (walls.contains(player)) {
                    System.out.println("Столкновение со стеной!");
                    player.setX(player.getX() + 1);
                }
                System.out.println("Игрок после выполнения команды: " + player);

                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setX(potion.getX() - 1);
                        System.out.println("Зелье после выполнения команды: " + potion);
                    }
                }
                break;
            case "right":
                player.setX(player.getX() + 1);
                if (walls.contains(player)) {
                    System.out.println("Столкновение со стеной!");
                    player.setX(player.getX() - 1);
                }
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setX(potion.getX() + 1);
                        System.out.println("Зелье после выполнения команды: " + potion);
                    }
                }
                break;
            default:
                return false;  // Неизвестное действие
        }
        System.out.println("Игрок после выполнения команды: " + player);

        // Проверка на выход за пределы поля
        if (player.getX() < 0 || player.getX() >= gridSize.getX() || player.getY() < 0 || player.getY() >= gridSize.getY()) {
            System.out.println("Выход за границы поля!");
            return false;
        }

        for (PositionDataModel potion : new ArrayList<>(potions)) { // Копируем список для избежания ошибок
        if (potion.equals(cauldron)) {
            potions.remove(potion);
        }
        }

        return true;
    } 

    private List<String> parseCommands(String command) {
        List<String> result = new ArrayList<>();
        StringBuilder currentCommand = new StringBuilder();
        int nestedLevel = 0;
    
        for (char c : command.toCharArray()) {
            if (c == '(') {
                nestedLevel++;
                currentCommand.append(c);
            } else if (c == ')') {
                nestedLevel--;
                currentCommand.append(c);
            } else if (c == ';' && nestedLevel == 0) {
                // Команда завершена, добавляем её в результат
                result.add(currentCommand.toString().trim());
                currentCommand.setLength(0); // Очищаем текущую команду
            } else {
                currentCommand.append(c);
            }
        }
    
        // Добавляем последнюю команду, если она не пустая
        if (currentCommand.length() > 0) {
            result.add(currentCommand.toString().trim());
        }
    
        return result;
    }
    

    @Override
    public List<Task> findTaskBySolutionId(Long idSolution) {
        return reposTask.findTaskBySolutionId(idSolution);
    }

}
