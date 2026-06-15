package com.duoc.LearningSystem.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.duoc.LearningSystem.dto.InscripcionDto;
import com.duoc.LearningSystem.service.InscripcionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<InscripcionDto> crearInscripcion(@RequestBody InscripcionDto inscripcionDto) {
        log.info("POST /api/inscripciones - solicitud recibida para crear inscripción");
        InscripcionDto inscripcionCreada = inscripcionService.crearInscripcion(inscripcionDto);
        log.info("POST /api/inscripciones - inscripción creada con número {}", inscripcionCreada.getNumeroResumen());
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionCreada);
    }

    @GetMapping("/{numeroResumen}")
    public ResponseEntity<InscripcionDto> obtenerInscripcion(@PathVariable Long numeroResumen) {
        log.info("GET /api/inscripciones/{} - solicitud recibida", numeroResumen);
        InscripcionDto inscripcion = inscripcionService.obtenerInscripcionPorNumero(numeroResumen);
        if (inscripcion == null) {
            log.warn("GET /api/inscripciones/{} - inscripción no encontrada", numeroResumen);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcion);
    }

    @GetMapping
    public List<InscripcionDto> listarInscripciones() {
        log.info("GET /api/inscripciones - solicitud recibida");
        return inscripcionService.listarInscripciones();
    }

    @GetMapping("/{numeroResumen}/generar-resumen")
    public ResponseEntity<String> generarResumen(@PathVariable Long numeroResumen) {
        log.info("GET /api/inscripciones/{}/generar-resumen - solicitud recibida", numeroResumen);
        InscripcionDto inscripcion = inscripcionService.obtenerInscripcionPorNumero(numeroResumen);
        if (inscripcion == null) {
            log.warn("GET /api/inscripciones/{}/generar-resumen - inscripción no encontrada", numeroResumen);
            return ResponseEntity.notFound().build();
        }
        String resumen = inscripcionService.generarResumen(inscripcion);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resumen-inscripcion-" + numeroResumen + ".txt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resumen);
    }

    @PostMapping("/{numeroResumen}/upload")
    public ResponseEntity<String> subirResumen(
            @PathVariable Long numeroResumen,
            @RequestParam String bucketName,
            @RequestParam MultipartFile file) throws IOException {
        log.info("POST /api/inscripciones/{}/upload - solicitud recibida", numeroResumen);
        String result = inscripcionService.subirResumenAS3(numeroResumen, bucketName, file);
        return ResponseEntity.ok("Resumen subido exitosamente: " + result);
    }

    @PutMapping("/{numeroResumen}/upload")
    public ResponseEntity<String> modificarResumen(
            @PathVariable Long numeroResumen,
            @RequestParam String bucketName,
            @RequestParam MultipartFile file) throws IOException {
        log.info("PUT /api/inscripciones/{}/upload - solicitud recibida", numeroResumen);
        String result = inscripcionService.modificarResumenEnS3(numeroResumen, bucketName, file);
        return ResponseEntity.ok("Resumen modificado exitosamente: " + result);
    }

    @GetMapping("/{numeroResumen}/download")
    public ResponseEntity<byte[]> descargarResumen(
            @PathVariable Long numeroResumen,
            @RequestParam String bucketName,
            @RequestParam String fileName) throws IOException {
        log.info("GET /api/inscripciones/{}/download - solicitud recibida", numeroResumen);
        byte[] contenido = inscripcionService.descargarResumenDeS3(numeroResumen, bucketName, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(contenido);
    }

    @DeleteMapping("/{numeroResumen}")
    public ResponseEntity<Void> borrarResumen(
            @PathVariable Long numeroResumen,
            @RequestParam String bucketName,
            @RequestParam String fileName) throws IOException {
        log.info("DELETE /api/inscripciones/{} - solicitud recibida", numeroResumen);
        inscripcionService.borrarResumenDeS3(numeroResumen, bucketName, fileName);
        return ResponseEntity.noContent().build();
    }
}