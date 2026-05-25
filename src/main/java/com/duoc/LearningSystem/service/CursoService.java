package com.duoc.LearningSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.LearningSystem.dto.CursoDto;
import com.duoc.LearningSystem.model.Curso;
import com.duoc.LearningSystem.repository.CursoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class CursoService {

	private final CursoRepository cursoRepository;

	public List<CursoDto> listarCursos() {
		List<CursoDto> cursos = cursoRepository.findAll().stream().map(this::toDto).toList();
		log.info("Servicio listarCursos ejecutado: {} curso(s) encontrados", cursos.size());
		return cursos;
	}

	public CursoDto obtenerCursoPorId(Long id) {
		CursoDto curso = cursoRepository.findById(id).map(this::toDto).orElse(null);
		if (curso == null) {
			log.warn("Servicio obtenerCursoPorId: curso {} no encontrado", id);
		} else {
			log.info("Servicio obtenerCursoPorId: curso {} encontrado ({})", id, curso.getNombre());
		}
		return curso;
	}

	public CursoDto crearCurso(CursoDto cursoDto) {
		Curso cursoGuardado = cursoRepository.save(toEntity(cursoDto));
		log.info("Servicio crearCurso: curso creado con id {} y nombre {}", cursoGuardado.getId(), cursoGuardado.getNombre());
		return toDto(cursoGuardado);
	}

	public CursoDto actualizarCurso(Long id, CursoDto cursoDto) {
		if (!cursoRepository.existsById(id)) {
			log.warn("Servicio actualizarCurso: curso {} no existe", id);
			return null;
		}

		Curso cursoActualizado = toEntity(cursoDto);
		cursoActualizado.setId(id);
		CursoDto cursoGuardado = toDto(cursoRepository.save(cursoActualizado));
		log.info("Servicio actualizarCurso: curso {} actualizado", id);
		return cursoGuardado;
	}

	public boolean eliminarCurso(Long id) {
		if (!cursoRepository.existsById(id)) {
			log.warn("Servicio eliminarCurso: curso {} no existe", id);
			return false;
		}

		cursoRepository.deleteById(id);
		log.info("Servicio eliminarCurso: curso {} eliminado", id);
		return true;
	}

	private Curso toEntity(CursoDto cursoDto) {
		if (cursoDto == null) {
			return null;
		}

		Curso curso = new Curso();
		curso.setId(cursoDto.getId());
		curso.setNombre(cursoDto.getNombre());
		curso.setInstructor(cursoDto.getInstructor());
		curso.setDuracionHoras(cursoDto.getDuracionHoras());
		curso.setCosto(cursoDto.getCosto());
		return curso;
	}

	private CursoDto toDto(Curso curso) {
		if (curso == null) {
			return null;
		}

		CursoDto cursoDto = new CursoDto();
		cursoDto.setId(curso.getId());
		cursoDto.setNombre(curso.getNombre());
		cursoDto.setInstructor(curso.getInstructor());
		cursoDto.setDuracionHoras(curso.getDuracionHoras());
		cursoDto.setCosto(curso.getCosto());
		return cursoDto;
	}
}