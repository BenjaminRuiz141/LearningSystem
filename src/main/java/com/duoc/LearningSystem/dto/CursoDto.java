package com.duoc.LearningSystem.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDto {

	private Long id;
	private String nombre;
	private String instructor;
	private Integer duracionHoras;
	private BigDecimal costo;
}