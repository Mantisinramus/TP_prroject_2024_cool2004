package com.example.main.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.model.SequenceOfPrimitives;

import jakarta.transaction.Transactional;

public interface SequnceRepository extends JpaRepository<SequenceOfPrimitives , Long> 
{

    //поиск id решения для id студента и id задачи
    // @Transactional
    // @Query("SELECT s.sequenceId FROM Solution s JOIN s.task t WHERE s.student.studentId = :studentId AND t.taskId = :taskId")
    // Long findSolutionIdByStudentAndTask(@Param("studentId") Long studentId, @Param("taskId") Long taskId);

}

