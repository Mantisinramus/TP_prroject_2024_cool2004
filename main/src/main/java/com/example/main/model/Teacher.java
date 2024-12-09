package com.example.main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Teacher {
    @Id
    @Column(name =  "teacher_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teacherId;
	
    @Column(name = "teacher_initials")
    private String teacherInitials;
	
	@Column(name = "teacher_login")
    private String teacherLogin;
	
    @Column(name = "teacher_password")
    private String teacherPassword;

}

