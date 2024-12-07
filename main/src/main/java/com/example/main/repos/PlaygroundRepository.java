package com.example.main.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main.model.Playground;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {

}

