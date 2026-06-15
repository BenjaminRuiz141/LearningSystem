package com.duoc.LearningSystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreEstudiante;

    @Column(nullable = false)
    private String emailEstudiante;

    @Column(nullable = false)
    private String nombreCurso;

    @Column(nullable = false)
    private String instructorCurso;

    @Column(nullable = false)
    private Integer duracionHoras;

    @Column(nullable = false)
    private BigDecimal costoCurso;

    @Column(nullable = false)
    private LocalDateTime fechaInscripcion;

    @Column(nullable = false, unique = true)
    private Long numeroResumen;
}