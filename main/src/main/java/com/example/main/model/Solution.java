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
public class Solution {
    @Id
    @Column(name =  "solution_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long solutionId;
	
    @Column(name = "mark")
    private int mark;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sequnce_id")
    private SequenceOfPrimitives  sequence;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Task task;

}

