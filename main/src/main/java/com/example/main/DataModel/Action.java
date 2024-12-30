package com.example.main.DataModel;

import java.util.List;



public class Action 
{
    private String action; // Название действия
    private int steps; // Количество шагов (только для одиночных действий)
    private int repeatCount; // Сколько раз повторить (только для циклов)
    private List<Action> subActions; // Вложенные действия (только для циклов)

    // Конструктор для простых действий
    public Action(String action, int steps) {
        this.action = action;
        this.steps = steps;
        this.repeatCount = 0;
        this.subActions = null;
    }

    // Конструктор для повторений
    public Action(String action, int repeatCount, List<Action> subActions) {
        this.action = action;
        this.repeatCount = repeatCount;
        this.subActions = subActions;
    }

    // Геттеры
    public String getAction() {
        return action;
    }

    public int getSteps() {
        return steps;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public List<Action> getSubActions() {
        return subActions;
    }

    public boolean isRepeat() {
        return "repeat".equals(action);
    }
}
