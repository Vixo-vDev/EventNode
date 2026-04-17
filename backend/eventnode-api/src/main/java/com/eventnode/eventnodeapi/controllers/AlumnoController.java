package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.AlumnoRegistroRequest;
import com.eventnode.eventnodeapi.services.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: endpoint de registro y actualizacion de alumnos en backend.
 * Por que existe: valida entrada HTTP y delega reglas de negocio al servicio de alumnos.
 */
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrar(@Valid @RequestBody AlumnoRegistroRequest request) {
        alumnoService.registrarAlumno(request);
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", "Cuenta creada con éxito");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> actualizarAlumno(
            @org.springframework.web.bind.annotation.PathVariable Integer id,
            @Valid @RequestBody com.eventnode.eventnodeapi.dtos.AlumnoActualizarRequest request) {
        alumnoService.actualizarAlumno(id, request);
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", "Alumno actualizado con éxito");
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> body = new HashMap<>();
        // Obtener el primer error de validación con su mensaje descriptivo
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Error de validación en los datos enviados");
        body.put("mensaje", mensaje);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        Map<String, String> body = new HashMap<>();
        String msg = ex.getMessage();
        if (msg != null && msg.contains("fechaNacimiento")) {
            body.put("mensaje", "Formato de fecha inválido. Use el formato AAAA-MM-DD");
        } else if (msg != null && msg.contains("cuatrimestre")) {
            body.put("mensaje", "El cuatrimestre debe ser un número válido");
        } else {
            body.put("mensaje", "Error en el formato de los datos enviados");
        }
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> manejarEstadoInvalido(IllegalStateException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarArgumentoInvalido(IllegalArgumentException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
}
