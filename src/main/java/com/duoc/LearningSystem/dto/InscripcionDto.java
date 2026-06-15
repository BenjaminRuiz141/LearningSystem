package com.duoc.LearningSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionDto {

    private Long id;
    private String nombreEstudiante;
    private String emailEstudiante;
    private String nombreCurso;
    private String instructorCurso;
    private Integer duracionHoras;
    private BigDecimal costoCurso;
    private LocalDateTime fechaInscripcion;
    private Long numeroResumen;
}