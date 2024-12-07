package com.example.main.repos;

import java.util.Queue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> 
{
    @Modifying
    @Transactional
    @Query("SELECT Mark FROM solution WHERE task_id =: idTask AND")
    void setMark(@Param("idTask") Long)

}

