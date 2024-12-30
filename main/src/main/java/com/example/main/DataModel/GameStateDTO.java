package com.example.main.DataModel;

import java.util.List;

public class GameStateDTO {
    private PositionDataModel playerPosition;
    private List<PositionDataModel> potions;
    private Boolean answer;

    // Конструкторы
    public GameStateDTO(PositionDataModel playerPosition, List<PositionDataModel> potions, Boolean answer) {
        this.playerPosition = playerPosition;
        this.potions = potions;
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

    public void setPotions(List<PositionDataModel> potions) {
        this.potions = potions;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }
}
