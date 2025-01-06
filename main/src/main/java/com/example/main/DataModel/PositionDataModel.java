package com.example.main.DataModel;

import org.springframework.stereotype.Component;

import jakarta.persistence.Embeddable;
import lombok.Data;


@Embeddable
@Data
@Component
public class PositionDataModel 
{
    private int x;
    private int y;
}
