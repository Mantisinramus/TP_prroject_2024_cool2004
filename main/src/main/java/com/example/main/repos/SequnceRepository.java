package com.example.main.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main.model.SequenceOfPrimitives;

public interface SequnceRepository extends JpaRepository<SequenceOfPrimitives , Long> {

}

