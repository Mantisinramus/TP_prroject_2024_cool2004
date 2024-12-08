package com.example.main.repos;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.model.Student;

import jakarta.transaction.Transactional;

public interface StudentRepository extends JpaRepository<Student, Long> 
{
    //поиск id студента, если нет, тогда обработать через else, потом дореализую
    @Transactional
    @Query("SELECT s.student_id FROM student s WHERE s.student_initials =:student_search")
    Long getIdStudentByInitials(@Param("student_search") String studentInitial);

}

