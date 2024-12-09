package com.example.main.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

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

    @Lob // Длинное текстовое поле для хранения JSON
    private String field; // JSON-структура поля
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}

