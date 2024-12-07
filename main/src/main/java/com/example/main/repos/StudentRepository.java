package com.example.main.repos;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> 
{

}

