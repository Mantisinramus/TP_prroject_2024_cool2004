package com.example.main.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main.model.Cell;

public interface CellRepository extends JpaRepository<Cell, Long> {

}
