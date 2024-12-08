package com.example.main.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.model.Solution;

import jakarta.transaction.Transactional;

public interface SolutionRepository extends JpaRepository<Solution, Long> 
{
    //поиск id оценки по id студента и id задачи , если нет, тогда обработать через else, потом дореализую
    // @Transactional
    // @Query("SELECT s.solutionId FROM Solution s WHERE s.task.taskId = :taskId AND s.task.teacher.student.studentId = :studentId")
    // Long findSolutionIdByStudentIdAndTaskId(@Param("studentId") Long studentId, @Param("taskId") Long taskId);

}

