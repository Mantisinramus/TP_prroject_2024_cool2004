package com.example.main.DataModel;

import jakarta.persistence.Embeddable;
import lombok.Data;


@Embeddable
@Data
public class PositionDataModel 
{
    private int x;
    private int y;
}
