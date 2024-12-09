package com.example.main.DataModel;

import java.util.List;

import lombok.Data;

@Data
public class TaskData 
{
    private GridSize gridSize;
    private Position player;
    private List<Position> potions;
    private Position cauldron;
    private List<Position> walls;

}
