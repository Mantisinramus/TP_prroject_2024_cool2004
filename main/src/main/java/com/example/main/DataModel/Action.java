package com.example.main.DataModel;

import java.util.List;

import lombok.Data;

@Data
public class Action 
{
    private String action;
    private int steps;
    private int times; // Количество повторений
    private List<Action> actions; //внутрянка repeat
}
