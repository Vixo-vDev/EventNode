package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.EventoCreateRequest;
import com.eventnode.eventnodeapi.services.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<Map<String, String>> crearEvento(@Valid @RequestBody EventoCreateRequest request) {
        Integer idUsuarioCreador = 1;
        eventoService.crearEvento(request, idUsuarioCreador);

        Map<String, String> body = new HashMap<>();
        body.put("mensaje", "Evento creado con éxito");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarArgumentos(IllegalArgumentException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> manejarEstado(IllegalStateException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
}

