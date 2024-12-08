package com.example.main.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.model.Teacher;

import jakarta.transaction.Transactional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> 
{

    //Поиск оценки
    // @Transactional
    // @Query("SELECT s.mark FROM Solution s " +"JOIN s.task t " +"JOIN t.student st " +"WHERE st.studentId = :studentId AND t.taskId = :taskId")
    // Integer findMarkByStudentAndTask(@Param("studentId") Long studentId, @Param("taskId") Long taskId);

    //поиск id учителя по его логину
    // @Transactional
    // @Query("SELECT t.teacherId FROM Teacher t WHERE t.teacherLogin = :teacherLogin")
    // Long findTeacherIdByLogin(@Param("teacherLogin") String teacherLogin);

}

