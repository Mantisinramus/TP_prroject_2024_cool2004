package com.example.main.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    
    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "student_taskList")
    private List<Task> tasks;

    public void addTask(Task addTask)
    {
        this.tasks.add(addTask);
    }

    public void deleteTask(Task deleteTask)
    {
        this.tasks.remove(deleteTask);
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}
