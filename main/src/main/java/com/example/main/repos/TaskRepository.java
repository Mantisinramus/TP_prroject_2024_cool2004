package com.example.main.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.main.model.Task;

import jakarta.transaction.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long> 
{
    @Transactional
    @Query(nativeQuery = true, value = " SELECT * FROM task  where task_id =:idSolution ")
    List<Task> findTaskBySolutionId(Long idSolution);

    // Метод для проверки, существует ли студент с таким логином
    Optional<Task> findByTaskName(String taskName);
}

