package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.AsistenciaRequest;
import com.eventnode.eventnodeapi.models.Asistencia;
import com.eventnode.eventnodeapi.services.AsistenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<Asistencia>> obtenerPorEvento(@PathVariable Integer idEvento) {
        return ResponseEntity.ok(asistenciaService.obtenerPorEvento(idEvento));
    }

    @PostMapping
    public ResponseEntity<?> registrarAsistencia(@RequestBody AsistenciaRequest request) {
        try {
            Asistencia asistencia = asistenciaService.registrarAsistencia(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(asistencia);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
