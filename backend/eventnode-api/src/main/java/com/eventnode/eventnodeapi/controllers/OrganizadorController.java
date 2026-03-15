package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.OrganizadorRequest;
import com.eventnode.eventnodeapi.models.Organizador;
import com.eventnode.eventnodeapi.services.OrganizadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organizadores")
public class OrganizadorController {

    private final OrganizadorService organizadorService;

    public OrganizadorController(OrganizadorService organizadorService) {
        this.organizadorService = organizadorService;
    }

    @GetMapping
    public ResponseEntity<List<Organizador>> obtenerTodos() {
        return ResponseEntity.ok(organizadorService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<?> crearOrganizador(@RequestBody OrganizadorRequest request) {
        try {
            Organizador org = organizadorService.crearOrganizador(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(org);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrganizador(@PathVariable Integer id, @RequestBody OrganizadorRequest request) {
        try {
            Organizador orgActualizado = organizadorService.actualizarOrganizador(id, request);
            return ResponseEntity.ok(orgActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOrganizador(@PathVariable Integer id) {
        try {
            organizadorService.eliminarOrganizador(id);
            return ResponseEntity.ok(Map.of("message", "Organizador eliminado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "No se puede eliminar el organizador porque está asociado a uno o más eventos."));
        }
    }
}
