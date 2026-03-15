package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.DiplomaRequest;
import com.eventnode.eventnodeapi.models.Diploma;
import com.eventnode.eventnodeapi.services.DiplomaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/diplomas")
public class DiplomaController {

    private final DiplomaService diplomaService;

    public DiplomaController(DiplomaService diplomaService) {
        this.diplomaService = diplomaService;
    }

    @PostMapping("/configurar")
    public ResponseEntity<?> configurarDiploma(@RequestBody DiplomaRequest request) {
        try {
            Diploma diploma = diplomaService.configurarDiploma(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(diploma);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
