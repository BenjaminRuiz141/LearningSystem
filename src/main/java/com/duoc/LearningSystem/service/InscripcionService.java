package com.duoc.LearningSystem.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.duoc.LearningSystem.aws.service.interfaces.AwsService;
import com.duoc.LearningSystem.dto.InscripcionDto;
import com.duoc.LearningSystem.model.Inscripcion;
import com.duoc.LearningSystem.repository.InscripcionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final AwsService awsService;

    public InscripcionDto crearInscripcion(InscripcionDto inscripcionDto) {
        Inscripcion inscripcion = toEntity(inscripcionDto);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setNumeroResumen(System.currentTimeMillis());
        
        Inscripcion guardada = inscripcionRepository.save(inscripcion);
        log.info("Servicio crearInscripcion: inscripción creada con número de resumen {}", guardada.getNumeroResumen());
        
        return toDto(guardada);
    }

    public InscripcionDto obtenerInscripcionPorNumero(Long numeroResumen) {
        Optional<Inscripcion> inscripcion = inscripcionRepository.findByNumeroResumen(numeroResumen);
        if (inscripcion.isPresent()) {
            log.info("Servicio obtenerInscripcionPorNumero: inscripción {} encontrada", numeroResumen);
            return toDto(inscripcion.get());
        }
        log.warn("Servicio obtenerInscripcionPorNumero: inscripción {} no encontrada", numeroResumen);
        return null;
    }

    public List<InscripcionDto> listarInscripciones() {
        List<InscripcionDto> inscripciones = inscripcionRepository.findAll().stream()
                .map(this::toDto)
                .toList();
        log.info("Servicio listarInscripciones: {} inscripciones encontradas", inscripciones.size());
        return inscripciones;
    }

    public String generarResumen(InscripcionDto inscripcionDto) {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DE INSCRIPCIÓN ===\n\n");
        resumen.append("Número de Resumen: ").append(inscripcionDto.getNumeroResumen()).append("\n");
        resumen.append("Fecha: ").append(inscripcionDto.getFechaInscripcion()).append("\n\n");
        resumen.append("--- DATOS DEL ESTUDIANTE ---\n");
        resumen.append("Nombre: ").append(inscripcionDto.getNombreEstudiante()).append("\n");
        resumen.append("Email: ").append(inscripcionDto.getEmailEstudiante()).append("\n\n");
        resumen.append("--- DATOS DEL CURSO ---\n");
        resumen.append("Curso: ").append(inscripcionDto.getNombreCurso()).append("\n");
        resumen.append("Instructor: ").append(inscripcionDto.getInstructorCurso()).append("\n");
        resumen.append("Duración: ").append(inscripcionDto.getDuracionHoras()).append(" horas\n");
        resumen.append("Costo: $").append(inscripcionDto.getCostoCurso()).append("\n");
        
        return resumen.toString();
    }

    public String subirResumenAS3(Long numeroResumen, String bucketName, MultipartFile file) throws IOException {
        Inscripcion inscripcion = inscripcionRepository.findByNumeroResumen(numeroResumen)
                .orElse(null);
        
        if (inscripcion == null) {
            log.warn("subirResumenAS3: inscripción {} no encontrada", numeroResumen);
            throw new IllegalArgumentException("Inscripción no encontrada con número: " + numeroResumen);
        }

        String key = numeroResumen + "/" + file.getOriginalFilename();
        String result = awsService.uploadObject(bucketName, key, file);
        log.info("subirResumenAS3: resumen subido a S3 con key {}", key);
        
        return result;
    }

    public String modificarResumenEnS3(Long numeroResumen, String bucketName, MultipartFile file) throws IOException {
        Inscripcion inscripcion = inscripcionRepository.findByNumeroResumen(numeroResumen)
                .orElse(null);
        
        if (inscripcion == null) {
            log.warn("modificarResumenEnS3: inscripción {} no encontrada", numeroResumen);
            throw new IllegalArgumentException("Inscripción no encontrada con número: " + numeroResumen);
        }

        String key = numeroResumen + "/" + file.getOriginalFilename();
        String result = awsService.uploadObject(bucketName, key, file);
        log.info("modificarResumenEnS3: resumen modificado en S3 con key {}", key);
        
        return result;
    }

    public byte[] descargarResumenDeS3(Long numeroResumen, String bucketName, String fileName) throws IOException {
        String key = numeroResumen + "/" + fileName;
        return awsService.downloadS3File(bucketName, key);
    }

    public void borrarResumenDeS3(Long numeroResumen, String bucketName, String fileName) throws IOException {
        String key = numeroResumen + "/" + fileName;
        awsService.deleteObject(bucketName, key);
        log.info("borrarResumenDeS3: resumen borrado de S3 con key {}", key);
    }

    private Inscripcion toEntity(InscripcionDto inscripcionDto) {
        if (inscripcionDto == null) {
            return null;
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(inscripcionDto.getId());
        inscripcion.setNombreEstudiante(inscripcionDto.getNombreEstudiante());
        inscripcion.setEmailEstudiante(inscripcionDto.getEmailEstudiante());
        inscripcion.setNombreCurso(inscripcionDto.getNombreCurso());
        inscripcion.setInstructorCurso(inscripcionDto.getInstructorCurso());
        inscripcion.setDuracionHoras(inscripcionDto.getDuracionHoras());
        inscripcion.setCostoCurso(inscripcionDto.getCostoCurso());
        inscripcion.setFechaInscripcion(inscripcionDto.getFechaInscripcion());
        inscripcion.setNumeroResumen(inscripcionDto.getNumeroResumen());
        
        return inscripcion;
    }

    private InscripcionDto toDto(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        InscripcionDto dto = new InscripcionDto();
        dto.setId(inscripcion.getId());
        dto.setNombreEstudiante(inscripcion.getNombreEstudiante());
        dto.setEmailEstudiante(inscripcion.getEmailEstudiante());
        dto.setNombreCurso(inscripcion.getNombreCurso());
        dto.setInstructorCurso(inscripcion.getInstructorCurso());
        dto.setDuracionHoras(inscripcion.getDuracionHoras());
        dto.setCostoCurso(inscripcion.getCostoCurso());
        dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
        dto.setNumeroResumen(inscripcion.getNumeroResumen());
        
        return dto;
    }
}