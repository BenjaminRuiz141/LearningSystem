package com.duoc.LearningSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.LearningSystem.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}