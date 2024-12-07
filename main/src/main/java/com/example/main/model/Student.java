package com.example.main.model;

import java.util.List;

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
public class Student{
    @Id
    @Column(name =  "student_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long studentId;
	
    @Column(name = "student_initials")
    private String studenrInitials;
	
	@Column(name = "student_login")
    private String studentLogin;
	
	@Column(name = "student_password")
    private String studentPassword;
    
    //спорная хуйня, так как никто на неё не дал ответ
    @Column(name = "student_taskList")
    private List<Long> studentTaskList;

    public void addTask(Long taskId)
    {
        this.studentTaskList.add(taskId);
    }

    public void deleteTask(Long taskId)
    {
        this.studentTaskList.remove(taskId);
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}
