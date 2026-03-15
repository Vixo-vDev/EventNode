package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.PreCheckinRequest;
import com.eventnode.eventnodeapi.models.PreCheckin;
import com.eventnode.eventnodeapi.services.PreCheckinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkin")
public class PreCheckinController {

    private final PreCheckinService preCheckinService;

    public PreCheckinController(PreCheckinService preCheckinService) {
        this.preCheckinService = preCheckinService;
    }

    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<PreCheckin>> obtenerPorEvento(@PathVariable Integer idEvento) {
        return ResponseEntity.ok(preCheckinService.obtenerPorEvento(idEvento));
    }

    @GetMapping("/mis-registros")
    public ResponseEntity<List<PreCheckin>> obtenerMisRegistros(Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(preCheckinService.obtenerPorEstudiante(correo));
    }

    @PostMapping("/pre")
    public ResponseEntity<?> realizarPreCheckin(@RequestBody PreCheckinRequest request, Authentication authentication) {
        try {
            String correo = authentication.getName();
            PreCheckin pc = preCheckinService.realizarPreCheckin(request, correo);
            return ResponseEntity.status(HttpStatus.CREATED).body(pc);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
