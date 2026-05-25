package com.duoc.LearningSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.LearningSystem.dto.CursoDto;
import com.duoc.LearningSystem.service.CursoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

	private final CursoService cursoService;

	@GetMapping
	public List<CursoDto> listarCursos() {
		log.info("GET /api/cursos - solicitud recibida");
		List<CursoDto> cursos = cursoService.listarCursos();
		log.info("GET /api/cursos - respuesta enviada con {} curso(s)", cursos.size());
		return cursos;
	}

	@GetMapping("/{id}")
	public ResponseEntity<CursoDto> obtenerCursoPorId(@PathVariable Long id) {
		log.info("GET /api/cursos/{} - solicitud recibida", id);
		CursoDto curso = cursoService.obtenerCursoPorId(id);
		if (curso == null) {
			log.warn("GET /api/cursos/{} - curso no encontrado", id);
			return ResponseEntity.notFound().build();
		}

		log.info("GET /api/cursos/{} - curso encontrado: {}", id, curso.getNombre());
		return ResponseEntity.ok(curso);
	}

	@PostMapping
	public ResponseEntity<CursoDto> crearCurso(@RequestBody CursoDto cursoDto) {
		log.info("POST /api/cursos - solicitud recibida para crear curso: {}", cursoDto.getNombre());
		CursoDto cursoCreado = cursoService.crearCurso(cursoDto);
		log.info("POST /api/cursos - curso creado con id {} y nombre {}", cursoCreado.getId(), cursoCreado.getNombre());
		return ResponseEntity.status(HttpStatus.CREATED).body(cursoCreado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CursoDto> actualizarCurso(@PathVariable Long id, @RequestBody CursoDto cursoDto) {
		log.info("PUT /api/cursos/{} - solicitud recibida para actualizar a {}", id, cursoDto.getNombre());
		CursoDto cursoActualizado = cursoService.actualizarCurso(id, cursoDto);
		if (cursoActualizado == null) {
			log.warn("PUT /api/cursos/{} - curso no encontrado para actualizar", id);
			return ResponseEntity.notFound().build();
		}

		log.info("PUT /api/cursos/{} - curso actualizado correctamente", id);
		return ResponseEntity.ok(cursoActualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
		log.info("DELETE /api/cursos/{} - solicitud recibida", id);
		if (!cursoService.eliminarCurso(id)) {
			log.warn("DELETE /api/cursos/{} - curso no encontrado para eliminar", id);
			return ResponseEntity.notFound().build();
		}

		log.info("DELETE /api/cursos/{} - curso eliminado correctamente", id);
		return ResponseEntity.noContent().build();
	}
}