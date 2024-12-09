package com.example.main.model;

import java.util.List;

import com.example.main.DataModel.PositionDataModel;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

//TODO столбцы верно расположить иначе кринж
@Data
@Entity
public class Task{
    @Id
    @Column(name =  "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
	
    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_text")
    private String taskText;

    @ElementCollection
    @CollectionTable(name = "task_potions")
    @AttributeOverrides({
        @AttributeOverride(name = "x", column = @Column(name = "potion_x")),
        @AttributeOverride(name = "y", column = @Column(name = "potion_y"))
    })
    private List<PositionDataModel> potions;

    @ElementCollection
    @CollectionTable(name = "task_walls")
    @AttributeOverrides({
        @AttributeOverride(name = "x", column = @Column(name = "wall_x")),
        @AttributeOverride(name = "y", column = @Column(name = "wall_y"))
    })
    private List<PositionDataModel> walls;

    @Embedded
    @Column(name = "task_player")
    @AttributeOverrides({
        @AttributeOverride(name = "x", column = @Column(name = "player_x")),
        @AttributeOverride(name = "y", column = @Column(name = "player_y"))
    })
    private PositionDataModel player;

    @Embedded
    @Column(name = "task_cauldron")
    @AttributeOverrides({
        @AttributeOverride(name = "x", column = @Column(name = "cauldron_x")),
        @AttributeOverride(name = "y", column = @Column(name = "cauldron_y"))
    })
    private PositionDataModel cauldron;

    @Embedded
    @Column(name = "task_gridSize")
    @AttributeOverrides({
        @AttributeOverride(name = "x", column = @Column(name = "length_x")),
        @AttributeOverride(name = "y", column = @Column(name = "length_y"))
    })
    private PositionDataModel gridSize;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}

