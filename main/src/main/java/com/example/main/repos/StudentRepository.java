package com.example.main.repos;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.model.Student;
import com.example.main.model.Task;

import jakarta.transaction.Transactional;

public interface StudentRepository extends JpaRepository<Student, Long> 
{
    //поиск id студента, если нет, тогда обработать через else, потом дореализую
    // @Transactional
    // @Query(nativeQuery = true, value = "SELECT s.student_id FROM Student s WHERE s.student_initials =:student_search")
    // Long getIdStudentByInitials(@Param("student_search") String studentInitial);

    // //поиск по id студента и id задачи само задание
    // @Transactional
    // @Query(nativeQuery = true, value = "SELECT t FROM Student s JOIN s.tasks t WHERE s.studentId = :studentId AND t.taskId = :taskId")
    // Task getTaskByStudentAndTaskId(@Param("studentId") Long studentId, @Param("taskId") Long taskId);
    
    // //поиск id студента по его логину
    @Transactional
    @Query(nativeQuery = true, value = "SELECT s.studentId FROM Student s WHERE s.studentLogin = :studentLogin")
    Long findStudentIdByLogin(@Param("studentLogin") String studentLogin);
}

