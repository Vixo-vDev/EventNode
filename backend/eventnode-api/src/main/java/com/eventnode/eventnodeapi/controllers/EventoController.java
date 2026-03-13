package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.EventoCreateRequest;
import com.eventnode.eventnodeapi.dtos.EventoUpdateRequest;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.services.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<?> consultarEventos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer categoriaId
    ) {
        List<Evento> eventos = eventoService.consultarEventosDisponibles(nombre, mes, categoriaId);
        if (eventos.isEmpty()) {
            Map<String, String> body = new HashMap<>();
            body.put("mensaje", "No se encontraron resultados");
            return ResponseEntity.status(HttpStatus.OK).body(body);
        }
        return ResponseEntity.ok(eventos);
    }

    @PutMapping("/{idEvento}")
    public ResponseEntity<Map<String, String>> actualizarEvento(@PathVariable Integer idEvento,
                                                               @RequestBody EventoUpdateRequest request) {
        eventoService.actualizarEvento(idEvento, request);
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", "Evento actualizado con éxito");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{idEvento}/cancelar")
    public ResponseEntity<Map<String, String>> cancelarEvento(@PathVariable Integer idEvento) {
        eventoService.cancelarEvento(idEvento);
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", "Evento cancelado con éxito");
        return ResponseEntity.ok(body);
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

