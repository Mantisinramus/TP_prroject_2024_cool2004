package com.example.main.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.main.DataModel.GameStateDTO;
import com.example.main.DataModel.PositionDataModel;
import com.example.main.model.Solution;
import com.example.main.model.Task;
import com.example.main.repos.SolutionRepository;
import com.example.main.repos.TaskRepository;
import com.example.main.repos.TeacherRepository;
import com.example.main.service.SolutionService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Primary
public class SolutionServiceImpl implements SolutionService
{

    private final SolutionRepository reposSolut;

    private final TaskRepository reposTask;

    private final TeacherRepository reposTeacher;

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

            gameStates = new ArrayList<>();
            // Получаем команды из sequenceText
            String сom = solution.getSequenceText();
            System.out.println("Размер поля: " + сom);
            String[] commands = сom.split(",");
        
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
            if (!potions.isEmpty()) {
                // Если gameStates не пустой, изменяем последний элемент
                if (!gameStates.isEmpty()) {
                    GameStateDTO lastState = gameStates.get(gameStates.size() - 1);
                    gameStates.set(gameStates.size() - 1, new GameStateDTO(
                        lastState.getPlayerPosition(),
                        lastState.getPotions(),
                        "Задача не решена!"
                    ));
                }
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
            String answer = performAction(singleCommand.trim());
            // Сохраняем текущее состояние
            PositionDataModel playerCopy = new PositionDataModel();
            playerCopy.setX(player.getX());
            playerCopy.setY(player.getY());
            boolean success;
            if(success = potions.size() == 0)
            {
                answer = "Успех";
            }
            List<PositionDataModel> potionsCopy = new ArrayList<>();
            for (PositionDataModel potion : potions) {
                PositionDataModel potionCopy = new PositionDataModel();
                potionCopy.setX(potion.getX());
                potionCopy.setY(potion.getY());
                potionsCopy.add(potionCopy);
            }
            System.out.println("Должно сохраниться " + potions);
            gameStates.add(new GameStateDTO(
                playerCopy, // Копия позиции игрока
                potionsCopy, // Копия списка зелий
                answer
            )
            );
        }
    }
}
    // Метод для выполнения одиночного действия
    private String performAction(String command) {
        System.out.println("Выполнение команды: " + command);
        String outAct = null;
        switch (command) {
            case "up":
                player.setY(player.getY() - 1);
                System.out.println("Игрок после выполнения команды: " + player);
                outAct = "Обычный шаг";
                if (walls.contains(player)) {
                    System.out.println("Столкновение со стеной!");
                    outAct = "Столкновение со стеной!";
                    player.setY(player.getY() + 1);
                }
                if (cauldron.equals(player)) {
                    System.out.println("Столкновение с котлом!");
                    outAct = "Столкновение с котлом!";
                    player.setY(player.getY() + 1);
                }
                if (player.getX() < 0 || player.getX() >= gridSize.getX() || player.getY() < 0 || player.getY() >= gridSize.getY()) {
                    System.out.println("Выход за границы поля!");
                    outAct = "Выход за границы поля!";
                    player.setY(player.getY() + 1);
                }
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setY(potion.getY() -1);
                        System.out.println("Зелье после выполнения команды: " + potion);
                        outAct = "Шаг с передвижением зелья";
                    }
                }
                break;
            case "down":
                player.setY(player.getY() + 1);
                System.out.println("Игрок после выполнения команды: " + player);
                outAct = "Обычный шаг";
                if (walls.contains(player)) {
                    System.out.println("Столкновение со стеной!");
                    outAct = "Столкновение со стеной!";
                    player.setY(player.getY() - 1);
                }
                if (cauldron.equals(player)) {
                    System.out.println("Столкновение с котлом!");
                    outAct = "Столкновение с котлом!";
                    player.setY(player.getY() - 1);
                }
                if (player.getX() < 0 || player.getX() >= gridSize.getX() || player.getY() < 0 || player.getY() >= gridSize.getY()) {
                    System.out.println("Выход за границы поля!");
                    outAct = "Выход за границы поля!";
                    player.setY(player.getY() - 1);
                }
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setY(potion.getY() + 1);
                        System.out.println("Зелье после выполнения команды: " + potion);
                        outAct = "Шаг с передвижением зелья";
                    }
                }
                break;
            case "left":
                player.setX(player.getX() - 1);
                System.out.println("Игрок после выполнения команды: " + player);
                outAct = "Обычный шаг";
                if (walls.contains(player)) {
                    System.out.println("Столкновение со стеной!");
                    outAct = "Столкновение со стеной!";
                    player.setX(player.getX() + 1);
                }
                if (cauldron.equals(player)) {
                    System.out.println("Столкновение с котлом!");
                    outAct = "Столкновение с котлом!";
                    player.setX(player.getX() + 1);
                }
                if (player.getX() < 0 || player.getX() >= gridSize.getX() || player.getY() < 0 || player.getY() >= gridSize.getY()) {
                    System.out.println("Выход за границы поля!");
                    outAct = "Выход за границы поля!";
                    player.setX(player.getX() + 1);
                }
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setX(potion.getX() - 1);
                        System.out.println("Зелье после выполнения команды: " + potion);
                        outAct = "Шаг с передвижением зелья";
                    }
                }
                break;
            case "right":
                player.setX(player.getX() + 1);
                System.out.println("Игрок после выполнения команды: " + player);
                outAct = "Обычный шаг";
                if (walls.contains(player)) {
                    System.out.println("Столкновение со стеной!");
                    outAct = "Столкновение со стеной!";
                    player.setX(player.getX() - 1);
                }
                if (cauldron.equals(player)) {
                    System.out.println("Столкновение с котлом!");
                    outAct = "Столкновение с котлом!";
                    player.setX(player.getX() - 1);
                }
                if (player.getX() < 0 || player.getX() >= gridSize.getX() || player.getY() < 0 || player.getY() >= gridSize.getY()) {
                    System.out.println("Выход за границы поля!");
                    outAct = "Выход за границы поля!";
                    player.setX(player.getX() - 1);
                }
                for (PositionDataModel potion : potions) {
                    if (player.getX() == potion.getX() && player.getY() == potion.getY()) 
                    {
                        potion.setX(potion.getX() + 1);
                        System.out.println("Зелье после выполнения команды: " + potion);
                        outAct = "Шаг с передвижением зелья";
                    }
                }
                break;
            default:
                return "неккоректная команда!";  // Неизвестное действие
        }
        for (PositionDataModel potion : new ArrayList<>(potions)) { // Копируем список для избежания ошибок
        if (potion.equals(cauldron)) {
            potions.remove(potion);
        }
        }
        return outAct;
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
    public Task randomGenerateTask(int rows, int cols, int numPotions, Long idTeacher, int counter) 
    {
        Random random = new Random();
        Set<PositionDataModel> usedPositions = new HashSet<>();

        // Генерация позиции игрока
        PositionDataModel player = getRandomPosition(rows, cols, usedPositions, random);

        // Генерация позиции котла
        PositionDataModel cauldron = getRandomPosition(rows, cols, usedPositions, random);

        PositionDataModel gridSize = new PositionDataModel();

        gridSize.setX(cols);
        gridSize.setY(rows);

         // Генерация зелий
        List<PositionDataModel> potions = new ArrayList<>();
        for (int i = 0; i < numPotions; i++) {
        PositionDataModel potion;
        do {
            potion = getRandomInnerPosition(rows, cols, usedPositions, random); // Только внутренние позиции
        } while (!isPotionAccessible(potion, usedPositions, rows, cols)); // Проверка доступности
        potions.add(potion);
        }

        // Генерация стен (случайное число стен до 1/4 от общего числа клеток)
        List<PositionDataModel> walls = new ArrayList<>();
        int maxWalls = (rows * cols) / 4;
        int numWalls = random.nextInt(maxWalls + 1);
        for (int i = 0; i < numWalls; i++) {
            PositionDataModel wall;
            do {
                wall = getRandomPosition(rows, cols, usedPositions, random);
            } while (isBlockingPotionAccess(wall, potions, usedPositions, rows, cols)); // Проверка блокировки
            walls.add(wall);
        }

            // Проверка на решаемость
        if (!isTaskSolvable(rows, cols, player, cauldron, potions, walls)) {
            System.out.println("Начало секса" + counter);
            Task newTask = randomGenerateTask(rows, cols, numPotions, idTeacher, counter + 1);
            return newTask; // Повторить генерацию
        }
        
        Task newTask = new Task();
        newTask.setTeacher(reposTeacher.findById(idTeacher).get());
        newTask.setTaskText("Перемести игрока, чтобы собрать " + potions.size() + " зелья и достичь котла.");
        newTask.setTaskName("Собери зелья");
        newTask.setWalls(walls);
        newTask.setGridSize(gridSize);
        newTask.setCauldron(cauldron);
        newTask.setPlayer(player);
        newTask.setPotions(potions);

        return newTask;
    }


    /**
     * Возвращает случайную позицию, исключая края.
     */
    private PositionDataModel getRandomInnerPosition(int rows, int cols, Set<PositionDataModel> usedPositions, Random random) {
        final int MAX_ATTEMPTS = 100; // Ограничение количества попыток
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            PositionDataModel position = new PositionDataModel();
            position.setY(random.nextInt(rows - 2) + 1); // Диапазон от 1 до rows - 2
            position.setX(random.nextInt(cols - 2) + 1); // Диапазон от 1 до cols - 2
            if (!usedPositions.contains(position)) {
                usedPositions.add(position);
                return position;
            }
        }
        throw new IllegalStateException("Не удалось найти свободную позицию за " + MAX_ATTEMPTS + " попыток");
    }
    /**
 * Проверка, достижимо ли зелье.
 */
private boolean isPotionAccessible(PositionDataModel potion, Set<PositionDataModel> usedPositions, int rows, int cols) {
    Queue<PositionDataModel> queue = new LinkedList<>();
    Set<PositionDataModel> visited = new HashSet<>();

    // Добавляем начальную позицию игрока
    queue.add(potion);
    visited.add(potion);

    int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Вправо, вниз, влево, вверх

    while (!queue.isEmpty()) {
        PositionDataModel current = queue.poll();

        // Проверяем соседние клетки
        for (int[] dir : directions) {
            int newX = current.getX() + dir[0];
            int newY = current.getY() + dir[1];
            PositionDataModel neighbor = new PositionDataModel();
            neighbor.setY(newY); // Диапазон от 1 до rows - 2
            neighbor.setX(newX); // Диапазон от 1 до cols - 2
            

            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols &&
                    !usedPositions.contains(neighbor) && !visited.contains(neighbor)) {
                visited.add(neighbor);
                queue.add(neighbor);

                // Если нашли свободное место — значит достижимо
                return true;
            }
        }
    }

    // Если не нашли свободное место
    return false;
}

/**
 * Генерация игрового поля.
 */
public void generateField(int rows, int cols, int numPotions, int numWalls, Random random) {
    Set<PositionDataModel> usedPositions = new HashSet<>();

    // Генерация игрока
    PositionDataModel player = getRandomInnerPosition(rows, cols, usedPositions, random);

    // Генерация котла
    PositionDataModel cauldron = getRandomInnerPosition(rows, cols, usedPositions, random);

    // Генерация зелий
    List<PositionDataModel> potions = new ArrayList<>();
    for (int i = 0; i < numPotions; i++) {
        PositionDataModel potion = getRandomInnerPosition(rows, cols, usedPositions, random);

        if (!isPotionAccessible(potion, usedPositions, rows, cols)) {
            throw new IllegalStateException("Позиция зелья недостижима");
        }

        potions.add(potion);
    }

    // Генерация стен
    for (int i = 0; i < numWalls; i++) {
        getRandomInnerPosition(rows, cols, usedPositions, random);
    }

    // Вывод результатов
    System.out.println("Игрок: " + player);
    System.out.println("Котёл: " + cauldron);
    System.out.println("Зелья: " + potions);
}






    /**
     * Проверяет, блокирует ли стена доступ к зелью.
     */
    private boolean isBlockingPotionAccess(PositionDataModel wall, List<PositionDataModel> potions, Set<PositionDataModel> usedPositions, int rows, int cols) {
        usedPositions.add(wall);
        for (PositionDataModel potion : potions) {
            if (!isPotionAccessible(potion, usedPositions, rows, cols)) {
                usedPositions.remove(wall); // Убираем стену из списка использованных, если она блокирует
                return true; // Блокирует доступ
            }
        }
        return false; // Не блокирует
    }

    private boolean isTaskSolvable(int rows, int cols, PositionDataModel player, PositionDataModel cauldron,List<PositionDataModel> potions, List<PositionDataModel> walls)
    {
    // Игровое поле как сет
    Set<PositionDataModel> obstacles = new HashSet<>(walls);
    obstacles.addAll(potions);

    // Проверяем, можно ли переместить каждое зелье к котлу
    for (PositionDataModel potion : potions) {
    if (!canMovePotionToCauldron(rows, cols, player, cauldron, potion, walls, new HashSet<>())) {
    return false; // Если хотя бы одно зелье невозможно дотащить, задача нерешаема
    }
    }

    // Проверяем, можно ли игроку дойти до котла
    return canReach(player, cauldron, rows, cols, obstacles);
    }


        /**
         * Проверяет, можно ли переместить зелье к котлу.
         */
        private boolean canMovePotionToCauldron(int rows, int cols, PositionDataModel player, PositionDataModel cauldron,
        PositionDataModel potion, List<PositionDataModel> walls,
        Set<PositionDataModel> visited) {
        // Если зелье уже на котле
        if (potion.equals(cauldron)) {
        return true;
        }

        // Множество препятствий, включая текущие зелья и стены
        Set<PositionDataModel> obstacles = new HashSet<>(walls);
        obstacles.add(potion); // Текущее зелье становится препятствием

        // BFS для проверки достижимости зелья
        Queue<PositionDataModel> queue = new LinkedList<>();
        queue.add(player);

        while (!queue.isEmpty()) {
        PositionDataModel current = queue.poll();

        // Проверяем, может ли игрок толкнуть зелье
        for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
        PositionDataModel next = new PositionDataModel();
        next.setX(current.getX() + dir[0]);
        next.setY(current.getY() + dir[1]);

        // Если игрок достигает зелья
        if (next.equals(potion)) {
        PositionDataModel pushedPotion = new PositionDataModel();
        pushedPotion.setX(potion.getX() + dir[0]);
        pushedPotion.setY(potion.getY() + dir[1]);
        if (isValidPosition(pushedPotion, rows, cols) && !obstacles.contains(pushedPotion)) {
            // Рекурсивно проверяем достижимость котла с новым положением зелья
            obstacles.remove(potion); // Убираем текущее зелье
            if (canMovePotionToCauldron(rows, cols, next, cauldron, pushedPotion, walls, visited)) {
                return true;
            }
            obstacles.add(potion); // Восстанавливаем текущее зелье
        }
    }

        // Если клетка доступна для игрока и не была посещена
        if (isValidPosition(next, rows, cols) && !visited.contains(next) && !obstacles.contains(next)) {
            queue.add(next);
            visited.add(next);
        }
    }
    }
    return false; // Невозможно дотащить зелье к котлу
    }

    /**
     * Проверяет, можно ли достичь цели (например, котла) из текущей позиции.
     */
    private boolean canReach(PositionDataModel start, PositionDataModel target, int rows, int cols, Set<PositionDataModel> obstacles) {
        Set<PositionDataModel> visited = new HashSet<>();
        Queue<PositionDataModel> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            PositionDataModel current = queue.poll();
            if (current.equals(target)) {
                return true; // Цель достигнута
            }

            for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                PositionDataModel next = new PositionDataModel();
                next.setX(current.getX() + dir[0]);
                next.setY(current.getY() + dir[1]);
                if (isValidPosition(next, rows, cols) && !visited.contains(next) && !obstacles.contains(next)) {
                    queue.add(next);
                    visited.add(next);
                }
            }
        }
        return false; // Цель недостижима
    }

    /**
     * Проверяет, находится ли позиция в пределах игрового поля.
     */
    private boolean isValidPosition(PositionDataModel position, int rows, int cols) {
        return position.getX() >= 0 && position.getX() < rows && position.getY() >= 0 && position.getY() < cols;
    }
        

    private static PositionDataModel getRandomPosition(int rows, int cols, Set<PositionDataModel> usedPositions, Random random) 
    {
    PositionDataModel posit = null;
    do {
        int x = random.nextInt(cols);
        int y = random.nextInt(rows);
        posit = new PositionDataModel();
        posit.setX(x);
        posit.setY(y);
    } while (usedPositions.contains(posit));
    usedPositions.add(posit);
    return posit;
    }

    @Override
    public List<Task> findTaskBySolutionId(Long idSolution) {
        return reposTask.findTaskBySolutionId(idSolution);
    }




}
