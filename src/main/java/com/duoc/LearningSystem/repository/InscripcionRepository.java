package com.duoc.LearningSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.LearningSystem.model.Inscripcion;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
    Optional<Inscripcion> findByNumeroResumen(Long numeroResumen);
    
    boolean existsByNumeroResumen(Long numeroResumen);
}