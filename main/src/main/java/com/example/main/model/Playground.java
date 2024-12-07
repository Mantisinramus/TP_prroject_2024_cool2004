package com.example.main.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Playground{
    @Id
    @Column(name =  "playground_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long playgroundId;
	
@Column(name = "size_x")
    private int sizeX;

@Column(name = "size_y")
    private int sizeY;

  @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}

