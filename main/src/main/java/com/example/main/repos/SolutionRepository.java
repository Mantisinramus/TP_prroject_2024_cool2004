package com.example.main.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.model.Solution;
import com.example.main.model.Task;

import jakarta.transaction.Transactional;

public interface SolutionRepository extends JpaRepository<Solution, Long> 
{
    //поиск id оценки по id студента и id задачи , если нет, тогда обработать через else, потом дореализую
    @Transactional
    @Query("SELECT s.solutionId FROM Solution s WHERE s.task.taskId = :task AND s.student.studentId = :student")
    Long findSolutionIdByStudentIdAndTaskId(@Param("student") Long student, @Param("task") Long task);

   
    @Transactional
    @Query(nativeQuery = true, value = " SELECT * FROM solution where student_id =:idStudent ")
    List<Solution> findAllByStudentId(Long idStudent);

    


}

