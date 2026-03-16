package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.EventoCreateRequest;
import com.eventnode.eventnodeapi.dtos.EventoResponse;
import com.eventnode.eventnodeapi.dtos.EventoUpdateRequest;
import com.eventnode.eventnodeapi.models.Categoria;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.repositories.CategoriaRepository;
import com.eventnode.eventnodeapi.services.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;
    private final CategoriaRepository categoriaRepository;

    public EventoController(EventoService eventoService, CategoriaRepository categoriaRepository) {
        this.eventoService = eventoService;
        this.categoriaRepository = categoriaRepository;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearEvento(@Valid @RequestBody EventoCreateRequest request) {
        try {
            eventoService.crearEvento(request);
            Map<String, String> body = new HashMap<>();
            body.put("mensaje", "Evento creado con éxito");
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (SecurityException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("mensaje", ex.getMessage());
            return ResponseEntity.status(403).body(body);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
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
        List<EventoResponse> response = eventos.stream()
                .map(e -> new EventoResponse(
                        e.getIdEvento(),
                        e.getBanner(),
                        e.getNombre(),
                        e.getUbicacion(),
                        e.getCapacidadMaxima(),
                        e.getTiempoCancelacionHoras(),
                        e.getFechaInicio(),
                        e.getFechaFin(),
                        e.getTiempoToleranciaMinutos(),
                        e.getDescripcion(),
                        e.getEstado(),
                        e.getCategoria() != null ? e.getCategoria().getIdCategoria() : null,
                        e.getCategoria() != null ? e.getCategoria().getNombre() : null
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Map<String, Object>>> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        List<Map<String, Object>> result = categorias.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idCategoria", c.getIdCategoria());
            map.put("nombre", c.getNombre());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getDefaultMessage())
                .orElse("Error de validación");
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", mensaje);
        return ResponseEntity.badRequest().body(body);
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
