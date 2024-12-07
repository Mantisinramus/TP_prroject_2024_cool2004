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
public class SequenceOfPrimitives {
    @Id
    @Column(name =  "sequnce_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sequenceId;
	
@Column(name = "sequnce_text")
    private String sequenceText;

  @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Student student;
}

