package com.duoc.LearningSystem.bootstrap;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.duoc.LearningSystem.model.Curso;
import com.duoc.LearningSystem.repository.CursoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Componente que se ejecuta al iniciar la aplicación para cargar datos de ejemplo en la base de datos

@Slf4j
@Component
@RequiredArgsConstructor
public class CursoDataLoader implements CommandLineRunner {

	private final CursoRepository cursoRepository;

	@Override
	public void run(String... args) {
		cursoRepository.deleteAll();
		cursoRepository.saveAll(getCursosDeEjemplo());
		log.info("CursoDataLoader: se cargaron {} cursos de ejemplo", cursoRepository.count());
	}

	private List<Curso> getCursosDeEjemplo() {
		return List.of(
				new Curso(null, "Fundamentos de Java", "Ana Torres", 24, new BigDecimal("45000")),
				new Curso(null, "Spring Boot desde cero", "Carlos Rojas", 30, new BigDecimal("55000")),
				new Curso(null, "Introducción a Docker", "María Pérez", 16, new BigDecimal("30000")),
				new Curso(null, "Git y GitHub Práctico", "Juan Soto", 12, new BigDecimal("25000")),
				new Curso(null, "Bases de Datos SQL", "Laura Díaz", 28, new BigDecimal("48000")),
				new Curso(null, "API REST con Spring", "Pedro Muñoz", 32, new BigDecimal("60000")),
				new Curso(null, "Microservicios Básicos", "Sofía Herrera", 36, new BigDecimal("72000")),
				new Curso(null, "Testing con JUnit", "Diego Castro", 18, new BigDecimal("32000")),
				new Curso(null, "Arquitectura de Software", "Valentina Silva", 20, new BigDecimal("40000")),
				new Curso(null, "Desarrollo Frontend Básico", "Ricardo Gómez", 26, new BigDecimal("50000")),
				new Curso(null, "HTML y CSS Esencial", "Camila Ruiz", 14, new BigDecimal("22000")),
				new Curso(null, "JavaScript para Principiantes", "Felipe Navarro", 22, new BigDecimal("35000")),
				new Curso(null, "Introducción a React", "Daniela Vega", 24, new BigDecimal("47000")),
				new Curso(null, "Control de Versiones Avanzado", "Martín León", 15, new BigDecimal("28000")),
				new Curso(null, "Buenas Prácticas de Código", "Patricia Flores", 10, new BigDecimal("20000")),
				new Curso(null, "Seguridad en Aplicaciones Web", "Héctor Bravo", 18, new BigDecimal("42000")),
				new Curso(null, "Despliegue en Docker", "Nicolás Castillo", 20, new BigDecimal("39000")),
				new Curso(null, "Fundamentos de Cloud", "Elena Arias", 21, new BigDecimal("41000")),
				new Curso(null, "Diseño de APIs", "Tomás Fuentes", 19, new BigDecimal("36000")),
				new Curso(null, "Pruebas de Integración", "Paula Romero", 17, new BigDecimal("33000")));
	}
}