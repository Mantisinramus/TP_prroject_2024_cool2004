package com.example.main.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Student{
    @Id
    @Column(name =  "student_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long studentId;
	
    @Column(name = "student_initials")
    private String studentInitials;
	
	@Column(name = "student_login")
    private String studentLogin;
	
	@Column(name = "student_password")
    private String studentPassword;

    @ElementCollection
    @CollectionTable(name = "student_tasks", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "task_id")
    private List<Long> tasksId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}
