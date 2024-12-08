package com.example.main.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> 
{

}

