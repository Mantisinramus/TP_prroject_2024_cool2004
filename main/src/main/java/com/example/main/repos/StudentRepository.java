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
    @Query("SELECT s.studentId FROM Student s WHERE s.studentInitials = :student")
    Long getIdStudentByInitials(@Param("student") String student);

    //поиск id студента по его логину
    @Transactional
    @Query("SELECT s.studentId FROM Student s WHERE s.studentLogin = :student")
    Long findStudentIdByLogin(@Param("student") String student);
}

