package com.example.main.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.model.Teacher;

import jakarta.transaction.Transactional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> 
{
    //поиск id учителя по его логину
    @Transactional
    @Query(nativeQuery = true, value = "SELECT t.teacher_id FROM teacher t WHERE t.teacher_login = :login")
    Long findTeacherIdByLogin(@Param("login") String login);

}

