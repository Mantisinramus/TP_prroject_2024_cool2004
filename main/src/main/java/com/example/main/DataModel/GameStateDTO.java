package com.example.main.DataModel;

import java.util.List;

public class GameStateDTO {
    private PositionDataModel playerPosition;
    private List<PositionDataModel> potions;
    private String answer;

    // Конструкторы
    public GameStateDTO(PositionDataModel playerPosition, List<PositionDataModel> potion, String answer) {
        this.playerPosition = playerPosition;
        this.potions = potion;
        this.answer = answer;
    }

    // Геттеры и сеттеры
    public PositionDataModel getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(PositionDataModel playerPosition) {
        this.playerPosition = playerPosition;
    }

    public List<PositionDataModel> getPotions() {
        return potions;
    }

    public void setPotions(List<PositionDataModel> potion) {
        this.potions = potion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
