package com.example.main.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data
@Entity
public class Cell{
    @Id
    @Column(name =  "cell_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cellId;
	
@Column(name = "postion_x")
    private int positionX;

@Column(name = "postion_y")
    private int positionY;

  @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "playground_id")
    private Playground playground;

	 @Column(name =  "type_of_cell")
    @Enumerated(EnumType.STRING)
    private CellType  typeOfCell;

}

