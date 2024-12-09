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
    @Query("SELECT t.teacherId FROM Teacher t WHERE t.teacherLogin = :login")
    Long findTeacherIdByLogin(@Param("login") String login);

}

